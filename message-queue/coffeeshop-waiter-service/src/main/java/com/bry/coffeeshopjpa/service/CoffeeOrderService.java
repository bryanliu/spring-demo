package com.bry.coffeeshopjpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.bry.coffeeshopjpa.model.Coffee;
import com.bry.coffeeshopjpa.model.CoffeeOrder;
import com.bry.coffeeshopjpa.model.OrderState;
import com.bry.coffeeshopjpa.repository.CoffeeOrderRepository;
import com.bry.coffeeshopjpa.support.Barista;

@Service
public class CoffeeOrderService {

    @Autowired CoffeeOrderRepository cor;

    @Autowired CoffeeService coffeeService;

    @Autowired Barista barista;

    public CoffeeOrder saveCoffeeOrder(String customer, List<String> coffeeItems) {

        List<Coffee> coffees = coffeeService.findCoffeeByNames(coffeeItems);

        CoffeeOrder order = CoffeeOrder.builder().customer(customer).items(coffees)
                .state(OrderState.INIT)
                .build();
        cor.save(order);

        //Send message
        barista.newOrders().send(MessageBuilder.withPayload(order.getId()).build());

        return order;
    }

    public List<CoffeeOrder> getAllOrders() {

        return cor.findAll();
    }

    public void deleteOrder(Integer id) {
        cor.deleteById(id);
    }
}
