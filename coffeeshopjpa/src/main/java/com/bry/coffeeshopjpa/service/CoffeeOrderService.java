package com.bry.coffeeshopjpa.service;

import java.util.Arrays;
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

    public CoffeeOrder saveCoffeeOrder(String customer, Coffee... coffee) {

        CoffeeOrder order = CoffeeOrder.builder().customer(customer).items(Arrays.asList(coffee))
                .state(OrderState.INIT)
                .build();
        cor.save(order);

        return order;
    }

    public List<CoffeeOrder> getALlOrders() {

        return cor.findAll();
    }

    public void deleteOrder(Integer id) {
        cor.deleteById(id);
    }
}
