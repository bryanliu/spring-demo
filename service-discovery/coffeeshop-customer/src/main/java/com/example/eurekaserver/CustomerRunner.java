package com.example.eurekaserver;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.eurekaserver.integration.CoffeeOrderService;
import com.example.eurekaserver.integration.CoffeeService;
import com.example.eurekaserver.model.Coffee;
import com.example.eurekaserver.model.CoffeeOrder;
import com.example.eurekaserver.model.NewOrderRequest;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomerRunner implements ApplicationRunner {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    CoffeeService coffeeService;

    @Autowired CoffeeOrderService coffeeOrderService;

    @Override public void run(ApplicationArguments args) throws Exception {

        discoveryClient.getInstances("waiter-service")
                .forEach(s -> log.info("waiter service: hots {} port {}", s.getHost(), s.getPort()));
        getMenuWithRestTemplateAndLoadBalancer();

        log.info("Get menu by feign client");
        getMenuWithFeigh();
        addCoffee();
    }

    void getMenuWithRestTemplateAndLoadBalancer() {
        ParameterizedTypeReference<List<Coffee>> cls = new ParameterizedTypeReference<List<Coffee>>() {
        };

        ResponseEntity<List<Coffee>>
                list = restTemplate.exchange("http://waiter-service/coffee/all", HttpMethod.GET, null, cls);
        list.getBody().forEach(coffee -> log.info("{}", coffee));

    }

    void getMenuWithFeigh() {
        List<Coffee> coffees = coffeeService.getAll();
        coffees.forEach(c -> log.info("{}", c));
    }

    void addCoffee(){
        NewOrderRequest req = NewOrderRequest.builder().customer("Bryan").items(Arrays.asList("mocha")).build();
        CoffeeOrder res = coffeeOrderService.addOrder(req);
        log.info("Add order {}", res);
    }
}
