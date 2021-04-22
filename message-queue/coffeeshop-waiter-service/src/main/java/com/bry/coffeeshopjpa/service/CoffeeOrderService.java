package com.bry.coffeeshopjpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bry.coffeeshopjpa.model.Coffee;
import com.bry.coffeeshopjpa.model.CoffeeOrder;
import com.bry.coffeeshopjpa.model.OrderState;
import com.bry.coffeeshopjpa.repository.CoffeeOrderRepository;

@Service
public class CoffeeOrderService {

    @Autowired CoffeeOrderRepository cor;

    @Autowired CoffeeService coffeeService;

    public CoffeeOrder saveCoffeeOrder(String customer, List<String> coffeeItems) {

        List<Coffee> coffees = coffeeService.findCoffeeByNames(coffeeItems);

        CoffeeOrder order = CoffeeOrder.builder().customer(customer).items(coffees)
                .state(OrderState.INIT)
                .build();
        cor.save(order);

        return order;
    }

    public List<CoffeeOrder> getAllOrders() {

        return cor.findAll();
    }

    public void deleteOrder(Integer id) {
        cor.deleteById(id);
    }
}
