package com.example.eurekaserver.integration;

import com.example.eurekaserver.model.CoffeeOrder;
import com.example.eurekaserver.model.NewOrderRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Component
public class FallbackCoffeeOrderService implements CoffeeOrderServiceClient {
    @Override public CoffeeOrder addOrder(NewOrderRequest request) {
        log.error("Error when create order, fallback");
        return null;
    }
}
