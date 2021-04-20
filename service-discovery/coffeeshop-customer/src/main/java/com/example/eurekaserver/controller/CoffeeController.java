package com.example.eurekaserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eurekaserver.integration.CoffeeService;
import com.example.eurekaserver.model.Coffee;

@RestController
@RequestMapping("/coffee")
public class CoffeeController {

    @Autowired CoffeeService coffeeService;

    @GetMapping("/")
    public List<Coffee> getAllCoffee() {
        return coffeeService.getAll();
    }

}
