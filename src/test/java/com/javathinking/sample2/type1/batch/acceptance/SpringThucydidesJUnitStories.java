package com.javathinking.sample2.type1.batch.acceptance;

import net.thucydides.jbehave.ThucydidesJUnitStories;
import net.thucydides.jbehave.ThucydidesStepFactory;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringThucydidesJUnitStories extends ThucydidesJUnitStories {

    @Override
    public InjectableStepsFactory stepsFactory() {
        return SpringAutowiringThucydidesStepFactory.withStepsFromPackage(getRootPackage(), configuration()).andClassLoader(getClassLoader());
    }

    public static class SpringAutowiringThucydidesStepFactory extends ThucydidesStepFactory {

        private static volatile ConfigurableApplicationContext ctx;
        private final static Object lock = new Object();

        private static void loadCtx() {
            synchronized (lock) {
                if (ctx == null) {
                    ctx = new ClassPathXmlApplicationContext("/acceptance-context.xml");
                    ctx.registerShutdownHook();
                }
            }
        }

        public SpringAutowiringThucydidesStepFactory(Configuration configuration, String rootPackage, ClassLoader classLoader) {
            super(configuration, rootPackage, classLoader);
            loadCtx();
        }

        @Override
        public Object createInstanceOfType(Class<?> type) {
            Object o = super.createInstanceOfType(type);
            ctx.getBeanFactory().autowireBeanProperties(
                    o, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
            return o;
        }

        public static ThucydidesStepFactory withStepsFromPackage(String rootPackage, Configuration configuration) {
            return new SpringAutowiringThucydidesStepFactory(configuration, rootPackage, defaultClassLoader());
        }

        private static ClassLoader defaultClassLoader() {
            return Thread.currentThread().getContextClassLoader();
        }
    }
}
