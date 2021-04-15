package com.bry.coffeeshopjpa.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.bry.coffeeshopjpa.model.Coffee;
import com.bry.coffeeshopjpa.service.CoffeeService;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc //不启动服务器,使用mockMvc进行测试http请求。启动了完整的Spring应用程序上下文，但没有启动服务器
@Slf4j
public class CoffeeControllerMockMVCTest {

    @Autowired MockMvc mvc;

    @MockBean CoffeeService coffeeService;

    @Test
    void testAddCoffeeSuccess() throws Exception {

        Coffee coffee = Coffee.builder().name("test1").price(200).build();

        Gson g = new Gson();
        log.info(g.toJson(coffee));
        //json.
        //cjson.toJSONString()
        mvc.perform(post("/coffee/")
                .contentType(APPLICATION_JSON)
                .content(g.toJson(coffee)))
                .andExpect(status().isCreated())
                //.andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andDo(print());
        verify(coffeeService, times(1)).addCoffee(coffee);

    }

    @Test
    void testGetCoffeeExists() throws Exception {
        Coffee coffee = Coffee.builder().name("test1").price(200).build();
        given(coffeeService.getCoffeeByName(any())).willReturn(Optional.ofNullable(coffee));

        mvc.perform(get("/coffee/exists")
                .accept(APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test1"))
                .andExpect(jsonPath("$.price").value("200"))
                .andDo(print())
        ;
    }

    @Test
    void testGetCoffeeNotExists() throws Exception {
        given(coffeeService.getCoffeeByName(any())).willReturn(Optional.empty());

        mvc.perform(get("/coffee/notexists"))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print())
        ;
    }

    @Test
    void testGetCoffeeByParamExists() throws Exception {
        Coffee coffee = Coffee.builder().name("test1").price(200).build();
        given(coffeeService.getCoffeeByName(any())).willReturn(Optional.ofNullable(coffee));

        mvc.perform(get("/coffee/")
                .param("name", "exists")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test1"))
                .andExpect(jsonPath("$.price").value("200"))
                .andDo(print())
        ;

        then(coffeeService).should(times(1)).getCoffeeByName("exists");
    }

    @Test
    void testGetCoffeeByParamExistsXML() throws Exception {
        Coffee coffee = Coffee.builder().name("test1").price(200).build();
        given(coffeeService.getCoffeeByName(any())).willReturn(Optional.ofNullable(coffee));

        mvc.perform(get("/coffee/")
                .param("name", "exists")
                .accept(MediaType.APPLICATION_XML)
        )
                .andExpect(status().isOk())
                //这里面放的也是Harmcrest的Matcher
                .andExpect(content().string(containsString("<Coffee><name>test1</name>")))
                .andExpect(xpath("/Coffee/price").string("200"))
                //具体jsonpath 和 xpath的使用，请参考 https://goessner.net/articles/JsonPath/
                .andDo(print())
        ;

        then(coffeeService).should(times(1)).getCoffeeByName("exists");
    }

    @Test
    void testAddCoffeeWithMoneyTypeAndFormType() throws Exception {

        mvc.perform(post("/coffee/addcoffeewithmoney")
                .param("name", "american")
                .param("price", "10.00")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)//以Form的方式调用
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(10.00))
                .andDo(print());

    }

    @Test
    void testAddCoffeeWithMoneyTypeAndFormTypeInvalid() throws Exception {
        // name is null
        mvc.perform(post("/coffee/addcoffeewithmoney")
                //.param("name", "american")
                .param("price", "10.00")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)//以Form的方式调用
        )
                .andExpect(status().isBadRequest())
                .andDo(print());

        // price is null
        mvc.perform(post("/coffee/addcoffeewithmoney")
                .param("name", "american")
                //.param("price", "10.00")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)//以Form的方式调用
        )
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    //@Disabled
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

    /*
    跑通了一个文件上传的例子，注意看Controller 里面的实现
     */
    @Test
    public void testUploadCoffees() throws Exception {
        //mvc.perform(fileUpload("/coffee/upload") //升级为multipart
        mvc.perform(multipart("/coffee/upload")
                .file(new MockMultipartFile("file", "test", "application/txt",
                        //new FileInputStream("coffeeuploadtest.txt")
                        //注意看是如何从Classpath获取文件的
                        getClass().getResourceAsStream("/coffeeuploadtest.txt")
                )))
                .andExpect(status().isOk())
                .andDo(print());

    }

}
