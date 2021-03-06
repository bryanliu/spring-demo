package com.bry.coffeeshopjpa.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.bry.coffeeshopjpa.model.Coffee;
import com.bry.coffeeshopjpa.service.CoffeeService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc //不启动服务器,使用mockMvc进行测试http请求。启动了完整的Spring应用程序上下文，但没有启动服务器
@Slf4j
class CoffeeOrderControllerMockMVCTest {

    @MockBean
    CoffeeService coffeeService;

    @Autowired
    CoffeeController coffeeController;

    @Autowired
    MockMvc mvc;

    @Test
    void testGetAllOrders() {

        given(coffeeService.getAllCofffees()).willReturn(Arrays.asList(
                Coffee.builder().name("c1").price(100).build(),
                Coffee.builder().name("c2").price(200).build()
        ));

        List<Coffee> coffees = coffeeController.getAllCoffee();

        assertThat(coffees).isNotNull();
        then(coffeeService).should(times(1)).getAllCofffees();

    }

    @Test
    /**
     * 如果要用到mockMVC的场景的时候，@InjectBean 就不合适了，因为 mockmvc 直接调用controller，而不会调用我们创建的controller.
     */
    void testGetAllCoffeeMvcTest() throws Exception {
        given(coffeeService.getAllCofffees()).willReturn(Arrays.asList(
                Coffee.builder().name("摩卡").price(100).build(),
                Coffee.builder().name("c2").price(200).build()
        ));
        mvc.perform(get("/coffee/all")
                .param("foo", "value")
                .param("bar", "value")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                //.andExpect(content().string("hello"))
                .andExpect(jsonPath("$[0].name").value("摩卡"))
                .andExpect(jsonPath("$[0].price").value("100"))
                .andExpect(jsonPath("$[1].name").value("c2"))
                .andExpect(jsonPath("$[1].price").value("200"))
                //.andExpect(jsonPath("$..name").value("200")) //  ["摩卡","c2"] 获得所有的Name
                .andDo(print());
    }

    @Test
    void testAddCoffeeFailEmptyContent() throws Exception {

        mvc.perform(
                post("/coffee/")
                        .contentType(APPLICATION_JSON)
                        .content("")
        )
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    void testAddCoffeeFailBadContent() throws Exception {

        mvc.perform(
                post("/coffee/")
                        .contentType(APPLICATION_JSON)
                        .content("{'name:'abc'}")
        )
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

}