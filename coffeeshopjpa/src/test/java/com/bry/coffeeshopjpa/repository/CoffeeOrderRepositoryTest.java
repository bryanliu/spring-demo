package com.bry.coffeeshopjpa.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import com.bry.coffeeshopjpa.model.CoffeeOrder;
import com.bry.coffeeshopjpa.model.OrderState;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class CoffeeOrderRepositoryTest {

    @Autowired CoffeeOrderRepository cor;

    @Autowired CoffeeRepository cr;

    @Test
    public void saveCoffeeOrder() {

        String customerName = "customer";

        CoffeeOrder co1 = CoffeeOrder.builder().customer(customerName)
                .items(cr.findCoffeeByNameInOrderById(Arrays.asList("latte", "espresso")))
                .state(OrderState.INIT)
                .build();
        cor.save(co1);
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("customer", exact());
        CoffeeOrder criteria = CoffeeOrder.builder().customer(customerName).build();
        Optional<CoffeeOrder> res = cor.findOne(Example.of(criteria, matcher));
        assertTrue(res.isPresent());
        assertEquals(customerName, res.get().getCustomer());
        assertEquals(OrderState.INIT, res.get().getState());
        //log.info("Got coffee order {}", res.get());

        //Cleanup data
        cor.findAll(Example.of(criteria, matcher)).forEach(order -> {
            log.info("cleanup test data {}", order.getId());
            cor.deleteById(order.getId());
        });
        //Check cleanup
        Optional<CoffeeOrder> res1 = cor.findOne(Example.of(criteria, matcher));
        assertFalse(res1.isPresent());

    }

}