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
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class Type1ExportStep {
    private DateTimeFormatter dateFormatter;

    @Resource
    private TransactionRepository transactionRepository;

    public Type1ExportStep() {
        dateFormatter = DateTimeFormat.forPattern("ddMMyyyy");
    }

    @Given("data is loaded with fileref \"$fileRef\" and transactions $examples")
    public void insertTransactions(String fileRef, ExamplesTable table) {
        List<Transaction> transactions = tableToTransactions(fileRef, table);
        transactionRepository.save(transactions);
    }

    private List<Transaction> tableToTransactions(String fileRef, ExamplesTable table) {
        List<Transaction> list = new ArrayList();
        for (Map<String, String> map : table.getRows()) {
            list.add(new Transaction(fileRef, map.get("client"), map.get("account"), dateFormatter.parseDateTime(map.get("date")), new BigDecimal(map.get("amount"))));
        }
        return list;
    }


}
