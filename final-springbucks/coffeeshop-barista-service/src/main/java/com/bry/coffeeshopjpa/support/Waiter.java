package com.bry.coffeeshopjpa.support;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Waiter {

    String NEW_ORDRS = "newOrders";
    String FINISHED_ORDRS = "finishedOrders";

    @Input
    MessageChannel newOrders();

    @Output
    SubscribableChannel finishedOrders();

}
