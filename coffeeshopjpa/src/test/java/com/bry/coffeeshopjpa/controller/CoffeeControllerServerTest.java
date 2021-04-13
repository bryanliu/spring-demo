package com.bry.coffeeshopjpa.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoffeeControllerServerTest {

    static String url = "http://localhost:";
    //是TestRestTemplate, 不是RestTemplate
    @Autowired TestRestTemplate rest;
    @MockBean CoffeeService coffeeService;
    @LocalServerPort
    Integer port;
    //不能这么写：String url = "http://localhost:"+port; 这个时候port还没初始化

    @BeforeAll
    /*
      终于解决了，beforeAll 只能是静态方法，导致不能初始化 url + port，
      port 是运行期注入的，但是在静态方法里面没法引用到 以及 和 url 组合起来
     */
    void beforeAll() {
        url = url + port;
    }

    @BeforeEach
    void beforeEach() {
        // Run before every test cases.
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
        List<Coffee> c = new Gson().fromJson(res, new TypeToken<List<Coffee>>() {
        }.getType());
        assertThat(c.size(), is(2));
        log.info(c.toString());
        // .i(Matchers.containsString("c1"));
    }
}
