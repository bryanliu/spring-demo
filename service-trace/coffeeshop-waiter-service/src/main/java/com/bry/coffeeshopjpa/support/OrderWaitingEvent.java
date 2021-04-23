package com.bry.coffeeshopjpa.support;

import org.springframework.context.ApplicationEvent;

import com.bry.coffeeshopjpa.model.CoffeeOrder;

import lombok.Data;

@Data
public class OrderWaitingEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param order the object on which the event initially occurred or with
     * which the event is associated (never {@code null})
     */
    private CoffeeOrder order;

    public OrderWaitingEvent(CoffeeOrder order) {
        super(order);
        this.order = order;

    }
}
