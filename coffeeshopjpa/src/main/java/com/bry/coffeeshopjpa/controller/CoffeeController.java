package com.bry.coffeeshopjpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bry.coffeeshopjpa.model.Coffee;
import com.bry.coffeeshopjpa.service.CoffeeService;

@RestController
@RequestMapping("/coffee")
public class CoffeeController {

    @Autowired CoffeeService coffeeService;

    @GetMapping("/all")
    public List<Coffee> getAllCoffee() {
        return coffeeService.getAllCofffees();
    }

    @PostMapping(value = "/"
            //            ,consumes = MediaType.APPLICATION_XML_VALUE,
            //            produces = MediaType.APPLICATION_XML_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public void saveCoffee(@RequestBody Coffee coffee) {
        coffeeService.addCoffee(coffee);
    }

    @GetMapping("/{name}")
    public Coffee getCoffee(@PathVariable String name) {

        return coffeeService.getCoffeeByName(name).orElse(null);

    }
}
