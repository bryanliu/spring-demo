package com.example.eurekaserver.controller;

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

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/coffee")
@Slf4j
public class CoffeeController {

    @Autowired CoffeeService coffeeService;

    @Autowired CoffeeOrderService coffeeOrderService;

    @GetMapping("/")
    //@HystrixCommand(fallbackMethod = "fallbackGetCoffee")
    public List<Coffee> getAllCoffee() {
        return coffeeService.getAll();
    }

    @PostMapping("/order")
    public CoffeeOrder addOrder(@RequestBody NewOrderRequest order) {
        return coffeeOrderService.addOrder(order);
    }

//    public List<Coffee> fallbackGetCoffee() {
//        log.error("Fallback for get coffee");
//        return null;
//    }

}
