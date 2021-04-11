package com.bry.coffeeshopjpa.service;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.bry.coffeeshopjpa.model.Coffee;
import com.bry.coffeeshopjpa.repository.CoffeeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@CacheConfig(cacheNames = "coffee")
public class CoffeeService {

    @Autowired
    private CoffeeRepository coffeeRepository;

    /**
     * Using JPA match to get data
     *
     * @param name: name of the coffee
     * @return coffee that match the result
     */
    @Cacheable
    public Optional<Coffee> getCoffeeByName(String name) {
        log.info("Get coffee {} from database", name);
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", exact());

        Optional<Coffee> result = coffeeRepository.findOne(Example.of(Coffee.builder().name(name).build(), matcher));
        log.info("Got result: {}", result);
        return result;

    }

    @CacheEvict()
    public void remove(String name) {
        log.info("do nothing, only show to clear the cache for coffee {}", name);
    }

    public void addCoffee(Coffee coffee) {

        coffeeRepository.save(coffee);
    }

    public void deleteCoffeeByName(String name) {
        coffeeRepository.deleteByName(name);
    }

    //@Cacheable
    public List<Coffee> getAllCofffees() {
        return coffeeRepository.findAll();
    }
}
