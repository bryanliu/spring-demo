package com.bry.coffeeshopjpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bry.coffeeshopjpa.model.Coffee;
import com.bry.coffeeshopjpa.service.CoffeeService;

@RestController
@RequestMapping("/coffee")
public class CoffeeController {

    @Autowired CoffeeService coffeeService;

    @GetMapping("/all")
    public List<Coffee> getAllCoffee(){
        return coffeeService.getAllCofffees();
    }

    @PostMapping("/")
    public void saveCoffee(@RequestBody Coffee coffee){
        coffeeService.addCoffee(coffee);
    }
}
