package com.example.eurekaserver.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.eurekaserver.model.CoffeeOrder;
import com.example.eurekaserver.model.NewOrderRequest;

@FeignClient(name = "waiter-service", contextId = "coffeeorder", path = "/order")
public interface CoffeeOrderServiceClient {

    @PostMapping("/order")
    CoffeeOrder addOrder(@RequestBody NewOrderRequest request);
}
