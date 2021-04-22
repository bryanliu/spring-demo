package com.bry.coffeeshopjpa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.bry.coffeeshopjpa.model.Coffee;
import com.bry.coffeeshopjpa.repository.CoffeeRepository;

@SpringBootTest
public class CoffeeServiceTest {

    @MockBean
    private CoffeeRepository coffeeRepo;

    @Autowired
    private CoffeeService coffeeService;

    @Test
    public void testGotCoffeeByPriceExists() {
        //arrange mock
        Coffee c = Coffee.builder().name("espresso").price(10).createTime(new Date()).build();
        given(coffeeRepo.findOne(any())).willReturn(Optional.ofNullable(c));

        //invoke 1
        Optional<Coffee> coffee = coffeeService.getCoffeeByName("espresso");

        //Capture and verify 1
        then(coffeeRepo).should(times(1)).findOne(any());
        assertTrue(coffee.isPresent());
        assertEquals("espresso", coffee.get().getName());

        //invoke 2
        coffee = coffeeService.getCoffeeByName("espresso");

        //Capture and verify 2
        //invoke time still once, since get from cache, not from repo
        then(coffeeRepo).should(times(1)).findOne(any());
    }

    @Test
    public void testHitCacheAndClearCache() {

        //arrange mock
        Coffee c = Coffee.builder().name("espresso").price(10).createTime(new Date()).build();
        given(coffeeRepo.findOne(any())).willReturn(Optional.ofNullable(c));

        //invoke
        Optional<Coffee> coffee1 = coffeeService.getCoffeeByName("espresso"); //No cache, will goto db

        //verify
        then(coffeeRepo).should(times(1)).findOne(any());

        //Got from cache
        coffee1 = coffeeService.getCoffeeByName("espresso");
        then(coffeeRepo).should(times(1)).findOne(any());

        //Remove from cache
        coffeeService.remove("espresso");

        coffee1 = coffeeService.getCoffeeByName("espresso");
        then(coffeeRepo).should(times(2)).findOne(any());

        //cleanup
        coffeeService.remove("espresso");
    }

    @Test
    public void testGetallCache() {
        /**
         * 这个测试主要用来测试JVM缓存的效果，如果没有缓存，都要从数据库读取，1000次循环，大概要1秒钟的时间
         * 如果开启了缓存，只要145 ms，即使到1W次，时间也差不多。
         * 改成Redis之后10000行明显慢了很多，大概要12s左右，一方面是本地的redis 性能有限，网络开销也不可忽视。
         * 而且之前都是内存数据库，内存缓存，所以速度会快很多。
         */
        List<Coffee> coffeeList = Arrays.asList(
                Coffee.builder().name("coffee1").price(20).build(),
                Coffee.builder().name("coffee2").price(30).build()
        );
        given(coffeeRepo.findAll()).willReturn(coffeeList);

        for (int i = 0; i < 100; i++) {
            List<Coffee> coffees = coffeeService.getAllCofffees();
        }

        then(coffeeRepo).should(times(1)).findAll();

    }

    @Nested
    @DisplayName("Cache Test")
    class CacheTest {

    }

    @Nested
    @DisplayName("JUnit 5 的新特性，可以将Case 分组")
    class NegativeCase {
        @Test
        public void testGetCoffeeByPriceNotExists() {
            //arrange mock
            given(coffeeRepo.findOne(any())).willReturn(Optional.empty());

            Optional<Coffee> coffee = coffeeService.getCoffeeByName("aa");

            then(coffeeRepo).should(times(1)).findOne(any());

            assertFalse(coffee.isPresent());
        }
    }

}