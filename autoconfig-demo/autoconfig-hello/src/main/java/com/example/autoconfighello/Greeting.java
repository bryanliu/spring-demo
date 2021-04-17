package com.example.autoconfighello;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Greeting implements ApplicationRunner {

    String context = "Spring";

    public Greeting() {
    }

    public Greeting(String context) {
        this.context = context;
    }

    @Override public void run(ApplicationArguments args) throws Exception {

        log.info("Running greeting from context {}", context);

    }
}
