package com.example.eurekaserver.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eurekaserver.integration.CoffeeOrderService;
import com.example.eurekaserver.integration.CoffeeService;
import com.example.eurekaserver.model.Coffee;
import com.example.eurekaserver.model.CoffeeOrder;
import com.example.eurekaserver.model.NewOrderRequest;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/coffee")
@Slf4j
public class CoffeeController {

    @Autowired CoffeeService coffeeService;

    @Autowired CoffeeOrderService coffeeOrderService;

    Bulkhead bulkhead;
    RateLimiter rateLimiter;
    private io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker;

    public CoffeeController(CircuitBreakerRegistry registry,
            BulkheadRegistry bulkheadRegistry,
            RateLimiterRegistry rateLimiterRegistry) {
        circuitBreaker = registry.circuitBreaker("menu");
        bulkhead = bulkheadRegistry.bulkhead("menu");
        rateLimiter = rateLimiterRegistry.rateLimiter("order");
    }

    @GetMapping("/")
    //@HystrixCommand(fallbackMethod = "fallbackGetCoffee")

    public List<Coffee> getAllCoffee() {
        return Try.ofSupplier(
                Bulkhead.decorateSupplier(bulkhead,
                        CircuitBreaker.decorateSupplier(circuitBreaker, () -> coffeeService.getAll())))
                .recover(Exception.class, Collections.emptyList()) //两种写法都可以
                //                .recover(throwable -> {
                //                    log.error("error happens when call get all coffees", throwable);
                //                    return Collections.emptyList();
                //                })
                .recover(BulkheadFullException.class, Collections.emptyList())
                .get();
    }

    @PostMapping("/order")
    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "order")
    @io.github.resilience4j.bulkhead.annotation.Bulkhead(name = "order")
    public CoffeeOrder addOrder(@RequestBody NewOrderRequest order) {
        return coffeeOrderService.addOrder(order);
    }

    //    public List<Coffee> fallbackGetCoffee() {
    //        log.error("Fallback for get coffee");
    //        return null;
    //    }

    @GetMapping("/allrl")
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "menu")
    public List<Coffee> getAllCoffeeRateLimit() {
        //用来演示rate Limit
        return coffeeService.getAll();

    }

    @PostMapping("/orderrl")
    public CoffeeOrder addOrderRL(@RequestBody NewOrderRequest order) {
        CoffeeOrder res = null;
        try {
            res = rateLimiter.executeSupplier(() -> coffeeOrderService.addOrder(order));
        } catch (RequestNotPermitted e) {
            log.warn("Request not permitted");
        }

        return res;

    }

}
