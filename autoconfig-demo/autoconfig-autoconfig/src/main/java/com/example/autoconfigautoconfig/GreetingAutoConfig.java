package com.example.autoconfigautoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.autoconfighello.Greeting;

@Configuration
@ConditionalOnClass(Greeting.class)
public class GreetingAutoConfig {

    @Bean
    @ConditionalOnMissingBean(Greeting.class)
    @ConditionalOnProperty(name = "greeting.enable", havingValue = "true"
            , matchIfMissing = true)
    public Greeting getGreeting() {
        return new Greeting("from autoconfig");
    }
}
