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
     * 这是一个路径参数的例子.
     * MockMVC 的例子见 CoffeeOrderControllerMockMVCTest.java
     *
     * @param name coffname
     * @return Coffee
     */
    @GetMapping("/{name}")
    public Coffee getCoffee(
            @PathVariable String name,
            @RequestParam(required = false) String id //设置为可选参数
    ) {

        log.info("Get coffee name {}, id {}", name, id);
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
    public CoffeeRequest addCoffeeWithMoneyType(@Valid CoffeeRequest coffee) {
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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadCoffee(@RequestParam("file") MultipartFile file) {

        if (file != null) {
            try (BufferedReader is = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                String str;
                while ((str = is.readLine()) != null) {
                    String[] splites = StringUtils.split(str);
                    if (splites != null && splites.length == 2) {
                        log.info("got coffee {}", str);
                        //Demo 演示效果，不调用Service了。
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 别太关注这个路径，主要是用来演示错误处理。  <br/>
     * 错误处理在 @see {@link GlobalControllerExceptionHandler} 中统一处理。
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
                orderProperties.getPrefix(), discount);
        return discount.toString();
    }
}
