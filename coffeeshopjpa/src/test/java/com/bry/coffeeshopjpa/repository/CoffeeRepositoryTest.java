package com.bry.coffeeshopjpa.repository;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bry.coffeeshopjpa.model.Coffee;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CoffeeRepositoryTest {

    @Autowired
    private CoffeeRepository coffeeRepository;

    @Test
    public void testGetCoffee() {

        Coffee coffee1 = coffeeRepository.findByName("espresso");
        assertNull(coffee1);

    }

}