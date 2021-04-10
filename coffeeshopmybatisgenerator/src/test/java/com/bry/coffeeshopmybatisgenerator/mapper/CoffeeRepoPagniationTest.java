package com.bry.coffeeshopmybatisgenerator.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bry.coffeeshopmybatisgenerator.model.auto.Coffee;
import com.bry.coffeeshopmybatisgenerator.model.auto.CoffeeExample;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class CoffeeRepoPagniationTest {

    @Autowired CoffeeRepo coffeeRepo;

    @Test
    public void testPaginationRowBound() {
        /*初始化是5条数据*/
        CoffeeExample example = new CoffeeExample();
        List<Coffee> coffeeList = coffeeRepo.selectByExampleWithRowbounds(example, new RowBounds(2, 2));
        assertAll(
                () -> assertEquals(2, coffeeList.size()),
                () -> assertEquals("capuccino", coffeeList.get(0).getName())
        );
        PageInfo page = new PageInfo(coffeeList); // 这儿的Page没有记录，总页数等信息
        log.info(page.toString());

        // late page, only have one record
        List<Coffee> coffeeList2 = coffeeRepo.selectByExampleWithRowbounds(example, new RowBounds(3, 2));
        assertAll(
                () -> assertEquals(1, coffeeList2.size()),
                () -> assertEquals("macchiato", coffeeList2.get(0).getName())
        );

        // limit = zero, will get all records and ignore offset
        List<Coffee> coffeeList3 = coffeeRepo.selectByExampleWithRowbounds(example, new RowBounds(3, 0));
        assertAll(
                () -> assertEquals(5, coffeeList3.size()),
                () -> assertEquals("espresso", coffeeList3.get(0).getName())
        );

    }

    @Test
    public void testPaginationWithParam() {
        List<Coffee> coffees = coffeeRepo.findAllwithParam(2, 2);
        assertEquals(2, coffees.size());
        assertEquals("capuccino", coffees.get(0).getName());
        PageInfo page = new PageInfo(coffees);
        log.info("{}", page); // PageInfo{pageNum=2, pageSize=2, size=2, startRow=3, endRow=4, total=5, pages=3,
        assertEquals(2, page.getPageNum());
        assertEquals(2, page.getPageSize());
        assertEquals(3, page.getStartRow());
        assertEquals(4, page.getEndRow());
        assertEquals(5, page.getTotal());
        assertEquals(3, page.getPages());

        coffees = coffeeRepo.findAllwithParam(-2, 2);
        log.info("{}", coffees);

    }
}
