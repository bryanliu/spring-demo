package com.bry.coffeeshopjpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bry.coffeeshopjpa.model.Coffee;

@Repository
public interface CoffeeRepository extends JpaRepository<Coffee, Integer> {

    Coffee findByName(String name);

    @Query(
            value = "select coffee from Coffee coffee order by coffee.price desc"
    )
    List<Coffee> listAllCoffees();

    void deleteByName(String name);
}
