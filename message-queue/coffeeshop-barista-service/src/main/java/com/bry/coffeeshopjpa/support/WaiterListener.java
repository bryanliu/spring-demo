package com.bry.coffeeshopjpa.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WaiterListener {

    @Autowired Waiter waiter;

    @StreamListener(Waiter.NEW_ORDRS)
    @SendTo(Waiter.FINISHED_ORDRS)
    public Integer newOrders(Integer id) throws InterruptedException {
        log.info("Get new orders {}", id);

        //Just echo back
        //Thread.sleep(10); //Sleep as process time
        //waiter.finishedOrders().send(MessageBuilder.withPayload(id).build());

        return id;

    }
}
