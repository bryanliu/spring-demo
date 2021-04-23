package com.bry.coffeeshopjpa.support;

import org.aspectj.lang.annotation.Around;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Barista {

    String NEW_ORDRS = "newOrders";
    String FINISHED_ORDRS = "finishedOrders";

    @Input
    SubscribableChannel finishedOrders();

    @Output
    MessageChannel newOrders();
}
