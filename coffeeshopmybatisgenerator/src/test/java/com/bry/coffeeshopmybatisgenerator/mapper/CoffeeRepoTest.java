package com.bry.coffeeshopmybatisgenerator.mapper;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bry.coffeeshopmybatisgenerator.model.auto.Coffee;

@SpringBootTest
@RunWith(SpringRunner.class)
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