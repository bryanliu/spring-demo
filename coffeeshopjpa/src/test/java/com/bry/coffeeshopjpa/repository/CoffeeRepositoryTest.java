package com.bry.coffeeshopjpa.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import com.bry.coffeeshopjpa.model.Coffee;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class CoffeeRepositoryTest {

    @Autowired
    private CoffeeRepository coffeeRepository;

    @Test
    public void testGetCoffeeExistsCustomizedMethod() {
        //测试自定义的find接口，根据名字查询，
        Coffee coffee1 = coffeeRepository.findByName("espresso");
        assertNotNull(coffee1);
        assertEquals("espresso", coffee1.getName());
    }

    @Test
    public void testGetCoffeeExistsByExample() {
        //使用Example 设置查询条件
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", exact());

        Optional<Coffee> result =
                coffeeRepository.findOne(Example.of(Coffee.builder().name("espresso").build(), matcher));
        log.info("Got result: {}", result);
        assertEquals("espresso", result.get().getName());
    }

    @Test
    public void testGetCoffeeNotExists() {
        Coffee coffee2 = coffeeRepository.findByName("notexists");
        assertNull(coffee2);

    }

    @Test
    public void testAddCoffee() {
        //测试默认的 save(), count() 接口
        Coffee coffee = Coffee.builder().name("bry").price(300).build();

        Coffee c = coffeeRepository.save(coffee);

        Coffee res = coffeeRepository.findByName("bry");
        assertNotNull(res);
        assertEquals("bry", res.getName());
        assertEquals(6, coffeeRepository.count());
        coffeeRepository.deleteById(c.getId()); // Clean the test data
        assertEquals(5, coffeeRepository.count());
    }

    @Test
    public void testDeleteCoffee() {
        // 测试删除接口
        Coffee c = coffeeRepository.findByName("espresso");

        coffeeRepository.deleteById(c.getId());

        assertEquals(4, coffeeRepository.count());

        coffeeRepository.save(c); // recover data
    }

    @Test
    public void testUpdateCoffee() {
        Coffee c = Coffee.builder().name("test1").price(40).build();
        // Insert a new object
        coffeeRepository.save(c);
        assertEquals("Total should increase", 6, coffeeRepository.count());
        c = coffeeRepository.findByName("test1");
        assertEquals("Price should be original", Integer.valueOf(40), c.getPrice());
        //Get and Update price
        c.setPrice(50);
        coffeeRepository.save(c);
        assertEquals("Total count should be same", 6, coffeeRepository.count());
        Coffee res = coffeeRepository.findByName("test1");
        assertEquals("Should be new price", Integer.valueOf(50), res.getPrice());
        //Cleanup the test data
        coffeeRepository.deleteById(res.getId());
        assertEquals(5, coffeeRepository.count());

    }

    @Test
    public void testGetAllCoffees() {
        // 测试自定义的listAllCoffees 接口，这是用SQL实现的
        List<Coffee> coffees = coffeeRepository.listAllCoffees();
        coffees.forEach(c -> {
            log.info("Got coffee {}", c);
        });

        assertEquals(5, coffees.size());
        assertEquals(Integer.valueOf(30), coffees.get(0).getPrice());
    }

    @Test
    public void testGetAllSorted() {
        // 测试排序
        Sort sort = Sort.by(Sort.Direction.ASC, "price");
        List<Coffee> coffees = coffeeRepository.findAll(sort);
        log.info(coffees.toString());
        assertEquals("sorted price by price asc, first price is 10",
                Integer.valueOf(10), coffees.get(0).getPrice());
        // findall by sort price desc
        sort = Sort.by(Sort.Direction.DESC, "price");
        coffees = coffeeRepository.findAll(sort);
        assertEquals("sorted price by price desc, should be 30",
                new Integer(30), coffees.get(0).getPrice());

    }

    @Test
    public void testDeleteAll() {
        //测试批量删除所有接口
        List<Coffee> coffees = coffeeRepository.findAll();
        assertEquals(5, coffees.size());

        coffeeRepository.deleteAllInBatch();
        //        coffeeRepository.deleteAll(); //这个方法会一条条删，效率没有上面一个高。
        List<Coffee> coffees2 = coffeeRepository.findAll();
        assertEquals(0, coffees2.size());

        // 恢复数据
        coffeeRepository.saveAll(coffees);
    }

    @Test
    public void testPagable() {
        Pageable pageable = PageRequest.of(1, 2);
        //Page 从0 开始，第二个参数是每页的数量
        Page<Coffee> page = coffeeRepository.findAll(pageable);

        assertEquals("总页数", 3, page.getTotalPages());
        assertEquals("总记录数", 5, page.getTotalElements());
        assertEquals("当前页数", 1, page.getNumber());
        assertEquals("当前页面的记录数", 2, page.getNumberOfElements());
        page.getContent().forEach(coffee -> log.info(coffee.toString()));

    }

}