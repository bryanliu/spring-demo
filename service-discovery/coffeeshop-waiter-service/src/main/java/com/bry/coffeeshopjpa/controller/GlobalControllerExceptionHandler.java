package com.bry.coffeeshopjpa.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public Map<String, String> handlerException(ValidationException e) {

        Map<String, String> map = new HashMap<>();
        map.put("message", e.getMessage());
        return map;

    }
}
