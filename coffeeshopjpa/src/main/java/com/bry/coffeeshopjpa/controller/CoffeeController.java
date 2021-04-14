package com.bry.coffeeshopjpa.controller;

import java.util.Date;
import java.util.List;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bry.coffeeshopjpa.controller.request.CoffeeRequest;
import com.bry.coffeeshopjpa.model.Coffee;
import com.bry.coffeeshopjpa.service.CoffeeService;

@RestController
@RequestMapping("/coffee")
public class CoffeeController {

    @Autowired CoffeeService coffeeService;

    @GetMapping("/all")
    public List<Coffee> getAllCoffee() {
        return coffeeService.getAllCofffees();
    }

    @PostMapping(value = "/"
            //            ,consumes = MediaType.APPLICATION_XML_VALUE,
            //            produces = MediaType.APPLICATION_XML_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public void saveCoffee(@RequestBody Coffee coffee) {
        coffeeService.addCoffee(coffee);
    }

    /**
     * 这是一个路径参数的例子.
     * MockMVC 的例子见 CoffeeOrderControllerMockMVCTest.java
     *
     * @param name coffname
     * @return Coffee
     */
    @GetMapping("/{name}")
    public Coffee getCoffee(@PathVariable String name) {

        return coffeeService.getCoffeeByName(name).orElse(null);

    }

    /**
     * 这是一个正常url参数的例子(?后面的一段)
     *
     * @param name coffee name
     * @return Coffee
     */
    @GetMapping("/")
    public Coffee getCoffeeByParam(@RequestParam String name) {

        return coffeeService.getCoffeeByName(name).orElse(null);

    }

    /**
     * 路径不是太符合Rest规范，主要用来和其他例子区分，这里面我们测试自定义类型转换
     * 我们将Price转换为Money，具体看MoneyFormatter。
     * 在这里面我们使用表单上传并bangdingcoffee对象。所以没有标注@RequestBody
     *
     * @param coffee - coffee数据
     * @return created Coffee
     */
    @PostMapping(value = "/addcoffeewithmoney",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public CoffeeRequest addCoffeeWithMoneyType(CoffeeRequest coffee) {
        // 只是用来演示，就不调用数据库了
        Coffee c = Coffee.builder()
                .name(coffee.getName())
                .price(coffee.getPrice().getAmountMajorInt())
                .createTime(new Date())
                .updateTime(new Date())
                .build();

        CoffeeRequest resp = CoffeeRequest.builder().name(coffee.getName())
                .price(Money.of(coffee.getPrice().getCurrencyUnit(), coffee.getPrice().getAmountMajorInt()))
                .build();

        return resp;
    }

    @PostMapping(value = "/addcoffeewithmoney")
    public CoffeeRequest addCoffeeWithMoneyType2(@RequestBody CoffeeRequest coffee) {

        // 只是用来演示，就不调用数据库了
        Coffee c = Coffee.builder()
                .name(coffee.getName())
                .price(coffee.getPrice().getAmountMajorInt())
                .createTime(new Date())
                .updateTime(new Date())
                .build();

        CoffeeRequest resp = CoffeeRequest.builder().name(coffee.getName())
                .price(Money.of(coffee.getPrice().getCurrencyUnit(), coffee.getPrice().getAmountMajorInt()))
                .build();

        return resp;
    }
}
