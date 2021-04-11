package com.bry.coffeeshopjpa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
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

    @Test
    public void testHitCacheAndClearCache() {
        Optional<Coffee> coffee1 = coffeeService.getCoffeeByName("espresso"); //No cache, will goto db
        Optional<Coffee> coffee2 = coffeeService.getCoffeeByName("latte");//No cache, will goto db
        coffee1 = coffeeService.getCoffeeByName("espresso"); //cached
        coffeeService.remove("espresso");
        coffee1 = coffeeService.getCoffeeByName("espresso"); //No cache, will goto db
        coffee2 = coffeeService.getCoffeeByName("latte"); //cached
    }

    @Test
    public void testGetallCache() {
        /**
         * 这个测试主要用来测试JVM缓存的效果，如果没有缓存，都要从数据库读取，1000次循环，大概要1秒钟的时间
         * 如果开启了缓存，只要145 ms，即使到1W次，时间也差不多。
         */

        for (int i = 0; i < 10000; i++) {
            List<Coffee> coffees = coffeeService.getAllCofffees();
        }

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