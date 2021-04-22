package com.example.eurekaserver.integration;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.eurekaserver.model.Coffee;

@FeignClient(name="waiter-service",  path = "/coffee")
public interface CoffeeServiceClient {

    @GetMapping(path="/all")
    List<Coffee> getAll();
}
