package com.bryan.springdemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class SpringdemoApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FooService fooService;

    @Test
    public void testInsertOneRowSuccess() {
        Long count = jdbcTemplate.queryForObject("select count(*) from foo where bar='CCC'", Long.class);
        assertEquals(new Long(0), count);
        //showdata();
        fooService.insertRow();
        //showdata();
        count = jdbcTemplate.queryForObject("select count(*) from foo where bar='CCC'", Long.class);
        assertEquals(count, new Long(1));

    }

    @Test
    public void testInsertWithException() {

        assertThrows(RollbackException.class, () -> fooService.insertWithException());
        //showdata();
        Long count = jdbcTemplate.queryForObject("select count(*) from foo where bar='DDD'", Long.class);
        assertEquals(new Long(0), count);
    }

    @Test
    @DisplayName("同级调用，事务回滚失效")
    public void testInsertExceptionNoRollback() {
        //showdata();
        assertThrows(RollbackException.class, () -> fooService.externalInsertwihtException());
        //showdata();
        Long count = jdbcTemplate.queryForObject("select count(*) from foo where bar='DDD'", Long.class);
        assertEquals(new Long(1), count);

    }

    private void showdata() {
        jdbcTemplate.queryForList("select * from foo").forEach(row -> log.info("got row: {}", row));
    }

    @Test
    public void testabc() {
        assertTrue(Boolean.TRUE);
    }

}
