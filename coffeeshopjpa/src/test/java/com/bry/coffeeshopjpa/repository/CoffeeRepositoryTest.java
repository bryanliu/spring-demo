package com.bry.coffeeshopjpa.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bry.coffeeshopjpa.model.Coffee;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class CoffeeRepositoryTest {

    @Autowired
    private CoffeeRepository coffeeRepository;

    @Test
    public void testGetCoffeeExists() {

        Coffee coffee1 = coffeeRepository.findByName("espresso");
        assertNotNull(coffee1);
        assertEquals("espresso", coffee1.getName());
    }

    @Test
    public void testGetCoffeeNotExists() {
        Coffee coffee2 = coffeeRepository.findByName("notexists");
        assertNull(coffee2);

    }

    @Test
    public void testAddCoffee() {
        Coffee coffee = Coffee.builder().name("bry").price(300).build();

        Coffee c = coffeeRepository.save(coffee);

        Coffee res = coffeeRepository.findByName("bry");
        assertNotNull(res);
        assertEquals("bry", res.getName());
        assertEquals(6, coffeeRepository.count());
        coffeeRepository.deleteById(c.getId()); // Clean the test data
        assertEquals(5, coffeeRepository.count());
    }

    @Test
    public void testDeleteCoffee() {
        Coffee c = coffeeRepository.findByName("espresso");

        coffeeRepository.deleteById(c.getId());

        assertEquals(4, coffeeRepository.count());

        coffeeRepository.save(c); // recover data
    }

    @Test
    public void testUpdateCoffee() {
        Coffee c = Coffee.builder().name("test1").price(40).build();
        // Insert a new object
        coffeeRepository.save(c);
        assertEquals("Total should increase", 6, coffeeRepository.count());
        c = coffeeRepository.findByName("test1");
        assertEquals("Price should be original", Integer.valueOf(40), c.getPrice());
        //Get and Update price
        c.setPrice(50);
        coffeeRepository.save(c);
        assertEquals("Total count should be same", 6, coffeeRepository.count());
        Coffee res = coffeeRepository.findByName("test1");
        assertEquals("Should be new price", Integer.valueOf(50), res.getPrice());
        //Cleanup the test data
        coffeeRepository.deleteById(res.getId());
        assertEquals(5, coffeeRepository.count());

    }

    @Test
    public void testGetAllCoffees() {

        List<Coffee> coffees = coffeeRepository.listAllCoffees();
        coffees.forEach(c -> {
            log.info("Got coffee {}", c);
        });

        assertEquals(5, coffees.size());
        assertEquals(Integer.valueOf(30), coffees.get(0).getPrice());
    }

}