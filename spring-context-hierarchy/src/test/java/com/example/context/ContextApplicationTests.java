package com.example.context;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

//@SpringBootTest
class ContextApplicationTests {

	@Test
	void contextLoads() {

		ApplicationContext rootContext = new AnnotationConfigApplicationContext(RootConfig.class);

		ClassPathXmlApplicationContext subContext = new ClassPathXmlApplicationContext(
				new String[]{"applicationContext.xml"}, rootContext
		);

		TestBean bean = rootContext.getBean("testBeanX", TestBean.class);
		bean.sayHello();

		bean = subContext.getBean("testBeanX", TestBean.class);
		bean.sayHello();

		bean = subContext.getBean("testBeanY", TestBean.class);
		bean.sayHello();
	}

}
