package com.bryan.springdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FooService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FooService self;

    @Transactional
    public void insertRow() {
        jdbcTemplate.execute("Insert into foo(bar) values ('CCC')");
    }

    @Transactional(rollbackFor = RollbackException.class)
    public void insertWithException() throws RollbackException {
        jdbcTemplate.execute("insert into foo(bar) values ('DDD')");
        throw new RollbackException();
    }

    public void externalInsertwihtException() throws RollbackException {
        insertWithException();
        //self.insertWithException(); // 加上注入的一个自己的实例，避免了同级失效的问题
    }

}
