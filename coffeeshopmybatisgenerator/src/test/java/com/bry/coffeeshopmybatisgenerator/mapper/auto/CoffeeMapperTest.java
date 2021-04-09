package com.bry.coffeeshopmybatisgenerator.mapper.auto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bry.coffeeshopmybatisgenerator.model.auto.Coffee;
import com.bry.coffeeshopmybatisgenerator.model.auto.CoffeeExample;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
class CoffeeMapperTest {

    @Autowired
    private CoffeeMapper coffeeMapper;

    @Test
    void insertSuccess() {

        Coffee c = new Coffee().withName("Test1").withPrice(200).withCreateTime(new Date())
                .withUpdateTime(new Date());
        int res = coffeeMapper.insert(c);
        //c = coffeeMapper.selectByPrimaryKey(1);
        log.info("res {}, coffee {}", res, c);
        assertEquals(1, res, "should insert 1");
        Coffee searchresult = coffeeMapper.selectByPrimaryKey(c.getId());
        assertEquals("Test1", searchresult.getName());

        //Remove
        coffeeMapper.deleteByPrimaryKey(searchresult.getId());

    }

    @Test
    void testUpdateSelective() {
        Coffee c = new Coffee().withId(1).withName("Test1").withPrice(200).withCreateTime(new Date())
                .withUpdateTime(new Date());
        int res = coffeeMapper.insert(c);

        // Only update updated fields
        Coffee update = new Coffee().withId(c.getId()).withName("Test2");

        coffeeMapper.updateByPrimaryKeySelective(update);

        Coffee searchresult = coffeeMapper.selectByPrimaryKey(update.getId());

        assertEquals("Test2", searchresult.getName());
        assertEquals(200, searchresult.getPrice());

        //Remove
        coffeeMapper.deleteByPrimaryKey(searchresult.getId());

    }

    @Test
    void testUpdateALl() {
        // 更新所有字段，如果这个字段没被设置，则会设为空（比较危险，平时少用）
        Coffee c = new Coffee().withId(1).withName("Test1").withPrice(200).withCreateTime(new Date())
                .withUpdateTime(new Date());
        int res = coffeeMapper.insert(c);

        // Only update updated fields
        Coffee update = new Coffee().withId(c.getId()).withName("Test2");

        coffeeMapper.updateByPrimaryKey(update);

        Coffee searchresult = coffeeMapper.selectByPrimaryKey(update.getId());

        assertEquals("Test2", searchresult.getName());
        assertEquals(null, searchresult.getPrice());

        //Remove
        coffeeMapper.deleteByPrimaryKey(searchresult.getId());

    }

    @Test
    public void testGetAll() {
        CoffeeExample example = new CoffeeExample();
        List<Coffee> l = coffeeMapper.selectByExample(example);
        assertEquals(5, l.size());
    }

    @Test
    public void testSearchByCriteria() {
        CoffeeExample example = new CoffeeExample();
        // =
        example.createCriteria().andNameEqualTo("latte");
        List<Coffee> l = coffeeMapper.selectByExample(example);
        assertEquals(1, l.size());

        // like： 1 criteria
        example.clear();
        example.createCriteria().andNameLike("m%");
        // example.createCriteria().andPriceEqualTo(20); //createCriteria 只能调用一次
        l = coffeeMapper.selectByExample(example);
        assertEquals(2, l.size());

        // like: several criteria
        example.clear();
        example.createCriteria().andNameLike("m%").andPriceEqualTo(21);
        // example.createCriteria().andPriceEqualTo(20); //createCriteria 只能调用一次
        l = coffeeMapper.selectByExample(example);
        assertEquals(1, l.size());
    }
}