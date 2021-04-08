package com.bry.coffeeshopjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bry.coffeeshopjpa.model.Coffee;

public interface CoffeeRepository extends JpaRepository<Coffee, Integer> {

    Coffee findByName(String name);
}
