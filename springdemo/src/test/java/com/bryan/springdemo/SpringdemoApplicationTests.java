package com.bryan.springdemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringdemoApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FooService fooService;

    @Test
    public void testinsertonerowsuccess() {

        fooService.insertRow();
        Long count = jdbcTemplate.queryForObject("select count(*) from foo where bar='CCC'", Long.class);
        assertEquals(count, new Long(2));

    }

    @Test(expected = RollbackException.class)
    public void testinsertwithException() throws RollbackException {
        fooService.insertWithException();
        Long count = jdbcTemplate.queryForObject("select count(*) from foo where bar='DDD'", Long.class);
        assertEquals(count, new Long(1));
    }

    @Test
    public void testabc() {
        assertTrue(Boolean.TRUE);
    }

}
