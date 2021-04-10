package com.bry.nosqldemo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import com.bry.nosqldemo.model.Coffee;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class CoffeeMongodbRepositoryTest {

    @Autowired CoffeeMongodbRepository coffeeMongodbRepository;

    @BeforeEach
    void precheckandinitial() {
        coffeeMongodbRepository.findByName("espresso").forEach(row -> coffeeMongodbRepository.deleteById(row.getId()));
        coffeeMongodbRepository.findByName("latte").forEach(row -> coffeeMongodbRepository.deleteById(row.getId()));
    }

    @AfterEach
    void aftercheckandcleanup() {
        coffeeMongodbRepository.findByName("espresso").forEach(row -> coffeeMongodbRepository.deleteById(row.getId()));
        coffeeMongodbRepository.findByName("latte").forEach(row -> coffeeMongodbRepository.deleteById(row.getId()));
    }

    @Test
    void testSavetoMongodb() {
        String coffeename = "espresso";

        Coffee espresso =
                Coffee.builder().name(coffeename).price(200).createTime(new Date()).updateTime((new Date())).build();

        //Save
        espresso = coffeeMongodbRepository.save(espresso);
        log.info("saved successfully {}", espresso);
        assertNotNull(espresso.getId());
        List<Coffee> res = coffeeMongodbRepository.findByName(coffeename);
        assertEquals(1, res.size());

    }

    @Test
    public void testSaveAll() {
        Coffee espresso =
                Coffee.builder().name("espresso").price(200).createTime(new Date()).updateTime((new Date())).build();
        Coffee latte =
                Coffee.builder().name("latte").price(300).createTime(new Date()).updateTime((new Date())).build();

        List<Coffee> res = coffeeMongodbRepository.saveAll(Arrays.asList(espresso, latte));
        res.forEach(row -> assertNotNull(row.getId()));

    }

    @Test
    void findByName() {
        Coffee espresso1 =
                Coffee.builder().name("espresso").price(200).createTime(new Date()).updateTime((new Date())).build();
        Coffee espresso2 =
                Coffee.builder().name("espresso").price(200).createTime(new Date()).updateTime((new Date())).build();

        coffeeMongodbRepository.saveAll(Arrays.asList(espresso1, espresso2));

        List<Coffee> res = coffeeMongodbRepository.findByName("espresso");
        assertEquals(2, coffeeMongodbRepository.findByName("espresso").size());
    }

    @Test
    @Disabled
    void findByExample() {

        Coffee espresso =
                Coffee.builder().name("espresso").price(200).createTime(new Date()).updateTime((new Date())).build();
        coffeeMongodbRepository.insert(espresso);
        coffeeMongodbRepository.findByName("espresso").forEach(row -> log.info("found {}", row));

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", exact());
        Optional<Coffee> result =
                coffeeMongodbRepository.findOne(Example.of(Coffee.builder().name("espresso").build(), matcher));

        assertTrue(result.isPresent());
        assertEquals(espresso.getPrice(), result.get().getPrice());

    }

    @Test
    void testUpdate() {
        Coffee espresso =
                Coffee.builder().name("espresso").price(200).createTime(new Date()).updateTime((new Date())).build();
        espresso = coffeeMongodbRepository.insert(espresso);
        Optional<Coffee> resbefore = coffeeMongodbRepository.findById(espresso.getId());
        assertTrue(resbefore.isPresent());
        assertEquals(200, resbefore.get().getPrice());

        espresso.setPrice(300);
        coffeeMongodbRepository.save(espresso);
        // coffeeMongodbRepository.insert(espresso); # insert 一定是插入数据，不会更新数据

        Optional<Coffee> res = coffeeMongodbRepository.findById(espresso.getId());

        assertTrue(res.isPresent());
        assertEquals(300, res.get().getPrice());

    }

}