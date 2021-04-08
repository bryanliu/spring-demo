package com.bry.coffeeshopjpa.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bry.coffeeshopjpa.model.Coffee;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CoffeeServiceTest {

    @Autowired
    private CoffeeService coffeeService;

    @Test
    public void testGetCoffeeByPriceNotExists() {

        Optional<Coffee> coffee = coffeeService.getCoffeeByName("aa");

        assertFalse(coffee.isPresent());
    }

    @Test
    public void testGotCoffeeByPriceExists() {
        Optional<Coffee> coffee = coffeeService.getCoffeeByName("espresso");

        assertTrue(coffee.isPresent());

        assertEquals("espresso", coffee.get().getName());
    }

}