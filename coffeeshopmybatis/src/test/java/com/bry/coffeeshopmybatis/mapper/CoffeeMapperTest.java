package com.bry.coffeeshopmybatis.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bry.coffeeshopmybatis.model.Coffee;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class CoffeeMapperTest {
    @Autowired
    CoffeeMapper coffeeMapper;

    @Test
    public void testSaveSuccess() {
        Coffee c = Coffee.builder().name("test").price(200).build();
        int insertCount = coffeeMapper.save(c);
        log.info("Saved {} successfully", c);
        assertEquals(1, insertCount, "insert count should be");
        // assertEquals(Integer.valueOf(6), c.getId());
        List<Coffee> allCoffees = coffeeMapper.findAll();
        assertEquals(6, allCoffees.size());
        // Clean the data
        coffeeMapper.deleteByName("test");
        assertEquals(5, coffeeMapper.count());

    }

    @Test
    public void testUpdate() {
        Coffee c = Coffee.builder().name("test").price(10).build();
        coffeeMapper.save(c);
        assertEquals(6, coffeeMapper.count());
        coffeeMapper.findAll().forEach(coff -> log.info(coff.toString()));

        c = coffeeMapper.findByName("test");
        assertNotNull(c, "should find the coffee");
        log.info("Got coffee {}", c);
        c.setName("test2");
        coffeeMapper.update(c);
        assertEquals(6, coffeeMapper.count());
    }

    @Test
    public void testSearchById() {

        Coffee coffee = coffeeMapper.findById(Integer.valueOf(1));
        assertNotNull(coffee);
        log.info("got coffee {}", coffee);
        assertEquals("espresso", coffee.getName(), "should got ");
    }

    @Test
    public void testFindAll() {
        List<Coffee> allCoffees = coffeeMapper.findAll();
        assertEquals(5, allCoffees.size());
        allCoffees.forEach(c -> log.info("Coffee {}", c));

    }

    @Test
    public void testDelete() {
        Coffee c = coffeeMapper.findByName("latte");

        coffeeMapper.deleteByName("latte");
        assertEquals(4, coffeeMapper.count());

        //Recover data
        coffeeMapper.save(c);
        coffeeMapper.findAll().forEach(coffee -> log.info("{}", coffee));
    }

}