package com.bry.coffeeshopjpa.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

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
        url += port;
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

        ParameterizedTypeReference<List<Coffee>> ptr = new ParameterizedTypeReference<List<Coffee>>() {
        };

        String res = rest.getForObject(url + "/coffee/all", String.class);
        assertThat(res, containsString("c1"));
        assertThat(res, containsString("c2"));
        List<Coffee> c = new Gson().fromJson(res, new TypeToken<List<Coffee>>() {
        }.getType());
        assertThat(c.size(), is(2));
        log.info(c.toString());
        // .i(Matchers.containsString("c1"));
    }

    @Test
    public void testGetAllCoffeeUsingParasTypeRef() {

        given(coffeeService.getAllCofffees()).willReturn(Arrays.asList(
                Coffee.builder().name("c1").price(100).build(),
                Coffee.builder().name("c2").price(200).build()
        ));

        //Using ParameterizedTypeReference and restTemplate.exchange()
        ParameterizedTypeReference<List<Coffee>> ptr = new ParameterizedTypeReference<List<Coffee>>() {
        };
        ResponseEntity<List<Coffee>> list = rest.exchange(url + "coffee/all", HttpMethod.GET, null, ptr);
        List<Coffee> coffees = list.getBody();

        assertThat(coffees.size(), is(2));
        log.info(coffees.toString());

        // 下面直接调用getForObject的话，会以 List<Map<String, String>>的形式返回。
        List res = rest.getForObject(url + "coffee/all", List.class);
        log.info(res.toString());

    }

    @Test
    void testRestUsingUriBuilder() {
        /*
         * 使用UriComponents 和 RequestEntity构建 请求
         * 使用Entity的方式，以获得更多的参数配置
         */

        //mock
        Coffee coffee = Coffee.builder().name("test1").price(200).build();
        given(coffeeService.getCoffeeByName(any())).willReturn(Optional.ofNullable(coffee));

        //init and invoke
        URI uri = UriComponentsBuilder
                .fromUriString(url + "coffee/{name}?id={id}")
                .build("test", "1");

        RequestEntity<Void> re = RequestEntity.get(uri)
                .accept(MediaType.APPLICATION_XML) // 可以设置MediaType
                .build();

        ResponseEntity<String> resp = rest.exchange(re, String.class);
        log.info("get result {}", resp.getBody());

        //verify
        then(coffeeService).should(times(1)).getCoffeeByName("test");
    }

}
