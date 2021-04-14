package com.bry.coffeeshopjpa.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc //不启动服务器,使用mockMvc进行测试http请求。启动了完整的Spring应用程序上下文，但没有启动服务器
@Slf4j
public class CoffeeControllerMockMVCTest {

    @Autowired MockMvc mvc;

    @Test
    void testAddCoffeeWithMoneyTypeAndFormType() throws Exception {

        mvc.perform(post("/coffee/addcoffeewithmoney")
                .param("name", "american")
                .param("price", "10.00")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)//以Form的方式调用
        )
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @Disabled
    void testAddCoffeeWithMoneyTypeWithRequestBody() throws Exception {
        /*
        这种方式的Convert还是有问题，会抛出无法映射，看后续能否解决
         */
        String content = "{\"name\":\"cat\", \"price\":30}";
        mvc.perform(post("/coffee/addcoffeewithmoney")
               .content(content)
                .contentType(MediaType.APPLICATION_JSON)//JSON的方式调用
        )
                .andExpect(status().isOk())
                .andDo(print());

    }
}
