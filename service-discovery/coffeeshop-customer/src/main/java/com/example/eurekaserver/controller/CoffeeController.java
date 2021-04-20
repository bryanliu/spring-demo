package com.example.eurekaserver.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eurekaserver.integration.CoffeeOrderService;
import com.example.eurekaserver.integration.CoffeeService;
import com.example.eurekaserver.model.Coffee;
import com.example.eurekaserver.model.CoffeeOrder;
import com.example.eurekaserver.model.NewOrderRequest;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/coffee")
@Slf4j
public class CoffeeController {

    @Autowired CoffeeService coffeeService;

    @Autowired CoffeeOrderService coffeeOrderService;

    private io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker;

    public CoffeeController(CircuitBreakerRegistry registry) {
        circuitBreaker = registry.circuitBreaker("menu");
    }

    @GetMapping("/")
    //@HystrixCommand(fallbackMethod = "fallbackGetCoffee")

    public List<Coffee> getAllCoffee() {
        return Try.ofSupplier(
                io.github.resilience4j.circuitbreaker.CircuitBreaker.decorateSupplier(
                        circuitBreaker, () -> coffeeService.getAll()))
                //.recover(Exception.class, Collections.emptyList()) //两种写法都可以
                .recover(throwable -> {
                    log.error("error happens when call get all coffees", throwable);
                    return Collections.emptyList();
                })
                .get();
    }

    @PostMapping("/order")
    @CircuitBreaker(name = "order")
    public CoffeeOrder addOrder(@RequestBody NewOrderRequest order) {
        return coffeeOrderService.addOrder(order);
    }

    //    public List<Coffee> fallbackGetCoffee() {
    //        log.error("Fallback for get coffee");
    //        return null;
    //    }

}
