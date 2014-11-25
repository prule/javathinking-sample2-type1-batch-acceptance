package com.javathinking.sample2.type1.batch.acceptance;

import com.javathinking.sample2.common.file.input.FileFormatContext;
import com.javathinking.sample2.common.file.input.FileFormatError;
import com.javathinking.sample2.common.file.input.FileParser;
import com.javathinking.sample2.type1.batch.input.ImportPipelineContext;
import com.javathinking.sample2.type1.batch.input.ImportService;
import com.javathinking.sample2.type1.transaction.Transaction;
import com.javathinking.sample2.type1.transaction.TransactionRepository;
import org.apache.commons.io.IOUtils;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class Type1ImportStep {
    private File data = new File("/tmp/data.txt");
    private ImportPipelineContext importPipelineContext;
    private FileFormatContext fileFormatContext;

    @Resource
    private ImportService importService;
    @Resource(name="fileFormat")
    private FileParser fileParser;
    @Resource
    private TransactionRepository transactionRepository;

    @Given("an input dataset of $data")
    public void withInputData(String content) throws IOException {
        FileWriter output = new FileWriter(data);
        IOUtils.write(content, output);
        output.close();
    }

    @When("importing the data")
    public void importData() throws IOException {
        importPipelineContext = importService.doImport(data);
        fileFormatContext = importPipelineContext.getFileFormatContext();
    }

    @When("validating the data")
    public void validateData() throws IOException {
        fileFormatContext = new FileFormatContext();
        fileParser.parse(new FileReader(data), fileFormatContext);
    }

    @Then("there should be \"$count\" file format errors")
    @Alias("there should be \"$count\" file format error")
    public void assertErrors(int count) {
        assertThat(fileFormatContext.getErrors().size(), is(count));
    }

    @Then("file format errors should include \"$message\"")
    public void assertErrors(String message) {
        boolean found=false;
        for (FileFormatError fileFormatError : fileFormatContext.getErrors()) {
            if(fileFormatError.getMessage().contains(message)) found = true;
        }
        assertThat("Could not find file format error "+message, found, is(true));
    }

    @Then("there should be \"$count\" transactions stored")
    public void assertTransactionCount(long count) {
        assertThat(transactionRepository.countByFileRef(importPipelineContext.getFileRef()), is(count));
    }

    @Then("the transactions should be categorized as $data")
    public void assertCategories(ExamplesTable data) {
        for (Map<String, String> map : data.getRows()) {
            String category = map.get("category");
            String amount = map.get("amount");
            List<Transaction> txns = transactionRepository.findAllByFileRefAndAmount(importPipelineContext.getFileRef(), new BigDecimal(amount));
            for (Transaction txn : txns) {
                assertThat(txn.getCategory(), is(category));
            }
        }
    }
}
