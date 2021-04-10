package com.bry.nosqldemo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bry.nosqldemo.model.Coffee;

public interface CoffeeMongodbRepository extends MongoRepository<Coffee, String> {

    List<Coffee> findByName(String name);
}
