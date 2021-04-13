package com.example.context;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
public class TestAspect {

    @AfterReturning("bean(testBean*)")
    public void printAfter() {
        log.info("After hello()");
    }
}
