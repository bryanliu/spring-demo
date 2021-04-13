package com.bry.coffeeshopjpa.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.web.JsonPath;

import com.bry.coffeeshopjpa.model.Coffee;
import com.bry.coffeeshopjpa.service.CoffeeService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;

/**
 * 这个测试会启动一个服务器，用于真实的HTTP 测试。但是会把依赖的Service Mock掉。
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class CoffeeControllerServerTest {

    //是TestRestTemplate, 不是RestTemplate
    @Autowired TestRestTemplate rest;
    @MockBean CoffeeService coffeeService;
    String url = "http://localhost:";
    @LocalServerPort
    private Integer port;
    //不能这么写：String url = "http://localhost:"+port; 这个时候port还没初始化

    @BeforeEach
    void beforeAll() {
        url = url + port;
    }

    @Test
    public void testGetAllCoffee() {

        log.info("Current port {}", port);

        given(coffeeService.getAllCofffees()).willReturn(Arrays.asList(
                Coffee.builder().name("c1").price(100).build(),
                Coffee.builder().name("c2").price(200).build()
        ));
        String res = rest.getForObject(url + "/coffee/all", String.class);
        assertThat(res, containsString("c1"));
        assertThat(res, containsString("c2"));
        assertThat("c1", equalTo("c1"));
        List<Coffee> c = new Gson().fromJson(res, new TypeToken<List<Coffee>>(){}.getType());
        assertThat(c.size(), is(2));
        log.info(c.toString());
        // .i(Matchers.containsString("c1"));
    }
}
