package com.bry.coffeeshopjpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bry.coffeeshopjpa.controller.request.OrderRequest;
import com.bry.coffeeshopjpa.model.CoffeeOrder;
import com.bry.coffeeshopjpa.service.CoffeeOrderService;

@RestController
@RequestMapping("/order")
public class CoffeeOrderController {

    @Autowired CoffeeOrderService coffeeOrderService;

    @PostMapping(value = "/order"
            //consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE
    )
    //@ResponseStatus(HttpStatus.CREATED)
    public CoffeeOrder makeOrder(@RequestBody OrderRequest request) {

        return coffeeOrderService.saveCoffeeOrder(request.getCustomer(), request.getItems());

    }

    @GetMapping("/all")
    public List<CoffeeOrder> getAllOrders() {
        return coffeeOrderService.getAllOrders();
    }

    @DeleteMapping("/")
    public void delete(@RequestParam Integer id) {
        coffeeOrderService.deleteOrder(id);
    }

}
