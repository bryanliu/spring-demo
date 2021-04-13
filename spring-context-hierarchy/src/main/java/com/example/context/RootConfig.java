package com.example.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class RootConfig {
    @Bean
    public TestBean testBeanX(){
        return new TestBean("root");
    }

    @Bean
    public TestBean testBeanY() {
        return new TestBean("root");
    }

    @Bean
    public TestAspect fooAspect(){
        return new TestAspect();
    }

}
