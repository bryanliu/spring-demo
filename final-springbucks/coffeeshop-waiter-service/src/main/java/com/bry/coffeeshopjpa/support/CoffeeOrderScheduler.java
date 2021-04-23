package com.bry.coffeeshopjpa.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;

import com.bry.coffeeshopjpa.model.CoffeeOrder;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CoffeeOrderScheduler {
    private Map<Integer, CoffeeOrder> orderMap = new HashMap<>();
    @EventListener
    public void acceptOrder(OrderWaitingEvent event) {
        log.info("Get order id {}", event.getOrder().getId());
        orderMap.put(event.getOrder().getId(), event.getOrder());

    }

    @Scheduled(fixedRate = 10000)
    public void waitingCoffee(){
        if(!orderMap.isEmpty()){

            log.info("I'm waiting for my coffee");
            orderMap.values().stream()
                    .map(order -> order.getId())
                    .forEach(orderid -> {
                log.info("I got my Coffee {}", orderid);
                //orderMap.remove(orderid);
            });
        }

        orderMap.clear();
    }
}
