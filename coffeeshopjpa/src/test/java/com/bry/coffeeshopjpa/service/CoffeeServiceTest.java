package com.bry.coffeeshopjpa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bry.coffeeshopjpa.model.Coffee;

@SpringBootTest
public class CoffeeServiceTest {

    @Autowired
    private CoffeeService coffeeService;

    @Test
    public void testGotCoffeeByPriceExists() {
        Optional<Coffee> coffee = coffeeService.getCoffeeByName("espresso");

        assertTrue(coffee.isPresent());

        assertEquals("espresso", coffee.get().getName());
    }

    @Nested
    @DisplayName("JUnit 5 的新特性，可以将Case 分组")
    class NegativeCase {
        @Test
        public void testGetCoffeeByPriceNotExists() {

            Optional<Coffee> coffee = coffeeService.getCoffeeByName("aa");

            assertFalse(coffee.isPresent());
        }
    }

}