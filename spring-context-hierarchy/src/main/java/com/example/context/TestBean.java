package com.example.context;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class TestBean {

    private String context;

    public void sayHello(){
        log.info("hello from context {}", context);
    }

}
