package com.bry.coffeeshopjpa;

import static org.assertj.core.util.DateUtil.now;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.bry.coffeeshopjpa.model.Coffee;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WhatEverTest {

    @Test
    public void testGSONConverter() {
        //Object Convert
        Gson g = new Gson();

        Coffee c = Coffee.builder().name("c1").price(100).createTime(now()).build();
        log.info("convert to json string {}", g.toJson(c));

        Coffee d = g.fromJson(g.toJson(c), Coffee.class);
        log.info("convert to json to object {}", d);

        //List Convert
        List<Coffee> coffees = Arrays.asList(
                Coffee.builder().name("c1").price(100).build(),
                Coffee.builder().name("c2").price(200).build()
        );

        log.info("convert to json string {}", g.toJson(coffees));

        List<Coffee> co2 = g.fromJson(g.toJson(coffees), new TypeToken<List<Coffee>>() {
        }.getType());
        log.info("convert to json to object {}", co2);

    }

}
