package com.bry.coffeeshopjpa.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.multipart.MultipartFile;

import com.bry.coffeeshopjpa.controller.request.CoffeeRequest;
import com.bry.coffeeshopjpa.model.Coffee;
import com.bry.coffeeshopjpa.service.CoffeeService;
import com.bry.coffeeshopjpa.support.OrderProperties;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/coffee")
@Slf4j
public class CoffeeController {

    @Autowired CoffeeService coffeeService;

    @Autowired OrderProperties orderProperties;

    @Value("${coffee.discount}") Integer discount;

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
     * ?????????????????????????????????.
     * MockMVC ???????????? CoffeeOrderControllerMockMVCTest.java
     *
     * @param name coffname
     * @return Coffee
     */
    @GetMapping("/{name}")
    public Coffee getCoffee(
            @PathVariable String name,
            @RequestParam(required = false) String id //?????????????????????
    ) {

        log.info("Get coffee name {}, id {}", name, id);
        return coffeeService.getCoffeeByName(name).orElse(null);

    }

    /**
     * ??????????????????url???????????????(????????????????)
     *
     * @param name coffee name
     * @return Coffee
     */
    @GetMapping("/")
    public Coffee getCoffeeByParam(@RequestParam String name) {

        return coffeeService.getCoffeeByName(name).orElse(null);

    }

    /**
     * ?????????????????????Rest???????????????????????????????????????????????????????????????????????????????????????
     * ?????????Price?????????Money????????????MoneyFormatter???
     * ???????????????????????????????????????bangdingcoffee???????????????????????????@RequestBody
     *
     * @param coffee - coffee??????
     * @return created Coffee
     */
    @PostMapping(value = "/addcoffeewithmoney",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public CoffeeRequest addCoffeeWithMoneyType(@Valid CoffeeRequest coffee) {
        // ?????????????????????????????????????????????
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

        // ?????????????????????????????????????????????
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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadCoffee(@RequestParam("file") MultipartFile file) {

        if (file != null) {
            try (BufferedReader is = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                String str;
                while ((str = is.readLine()) != null) {
                    String[] splites = StringUtils.split(str);
                    if (splites != null && splites.length == 2) {
                        log.info("got coffee {}", str);
                        //Demo ????????????????????????Service??????
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ???????????????????????????????????????????????????????????????  <br/>
     * ??????????????? @see {@link GlobalControllerExceptionHandler} ??????????????????
     */
    @GetMapping("/exception")
    public void getException() {
        log.info("will throw a exception from this method");
        throw new ValidationException("What ever");
    }

    @GetMapping("/config")
    public String getConfig() {
        //This mapping just used to demo the dynamic configuration
        log.info("This mapping just used to demo the dynamic configuration, coffee prefix {}, coffee discount {}",
                orderProperties.getPrefix(), orderProperties.getDiscount());
        return discount.toString();
    }
}
