package com.example.context;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class ContextApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(ContextApplication.class, args);
    }

    @Override public void run(ApplicationArguments args) throws Exception {

        ApplicationContext rootContext = new AnnotationConfigApplicationContext(RootConfig.class);

        ClassPathXmlApplicationContext subContext = new ClassPathXmlApplicationContext(
                new String[] { "applicationContext.xml" }, rootContext
        );

        TestBean bean = rootContext.getBean("testBeanX", TestBean.class);
        bean.sayHello();

        bean = subContext.getBean("testBeanX", TestBean.class);
        bean.sayHello();

        bean = subContext.getBean("testBeanY", TestBean.class);
        bean.sayHello();

    }
}
