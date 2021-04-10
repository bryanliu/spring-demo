package com.bry.nosqldemo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bry.nosqldemo.model.Coffee;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@SpringBootTest
@Slf4j
public class JedisTest {

    @Autowired private JedisPool jedisPool;

    @Test
    void testStoreAndFetch() {

        Coffee c1 = Coffee.builder().name("test1").price(10).build();
        Coffee c2 = Coffee.builder().name("test2").price(20).build();
        // try with resource 的方式，不用显式关闭了
        try (Jedis jedis = jedisPool.getResource()) {
            Arrays.asList(c1, c2).forEach(c -> {
                jedis.hset("menu", c.getName(), Integer.toString(c.getPrice()));
                jedis.zadd("sortmenu", c.getPrice(), c.getName());
            });

            // 获取所有的key value pairs
            Map<String, String> res = jedis.hgetAll("menu");
            log.info(res.toString());
            assertEquals("20", res.get("test2"));

            // 根据特定的field 获取value
            String price = jedis.hget("menu", "test1");
            log.info(price);
            assertEquals("10", price);

            // 有序集合，可以根据score的范围查找，删除
            jedis.zrangeByScore("sortmenu", 10, 20).forEach(c -> log.info(c));
            jedis.zremrangeByScore("sortmenu", 10, 20);
            jedis.zrangeByScore("sortmenu", 10, 20).forEach(c -> log.info(c));

        }
    }
}
