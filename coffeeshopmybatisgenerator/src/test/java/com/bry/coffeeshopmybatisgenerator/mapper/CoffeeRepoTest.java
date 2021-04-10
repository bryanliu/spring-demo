package com.bry.coffeeshopmybatisgenerator.mapper;


import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bry.coffeeshopmybatisgenerator.model.auto.Coffee;

@SpringBootTest
public class CoffeeRepoTest {

    @Autowired
    private CoffeeRepo coffeeRepo;

    @Test
    public void searchByName() {

        Coffee c = coffeeRepo.searchByName("latte");
        assertNotNull(c);

    }

    @Test
    public void searchByNameXML() {

        Coffee c = coffeeRepo.searchByNameinXMl("latte");
        assertNotNull(c);

    }

}