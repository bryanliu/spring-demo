package com.bryan.springdemo;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@RestController
//@EnableTransactionManagement(mode= AdviceMode.ASPECTJ)
public class SpringdemoApplication implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FooService fooService;

    public static void main(String[] args) {
        SpringApplication.run(SpringdemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        //        showConnection();
//        //        showdata();
//        fooService.insertRow();
//        log.info("CCC count {}", jdbcTemplate.queryForObject("select count(*) from foo where bar='CCC'", Long.class));
//
//        try {
//            fooService.insertWithException();
//        } catch (RollbackException e) {
//            log.info("DDD count {}",
//                    jdbcTemplate.queryForObject("select count(*) from foo where bar='DDD'", Long.class));
//            e.printStackTrace();
//        }
//
//        try {
//            fooService.externalInsertwihtException();
//        } catch (RollbackException e) {
//            log.info("DDD count {}",
//                    jdbcTemplate.queryForObject("select count(*) from foo where bar='DDD'", Long.class));
//            e.printStackTrace();
//        }
//
//        log.info("DDD count {}", jdbcTemplate.queryForObject("select count(*) from foo where bar='DDD'", Long.class));

    }

    private void showConnection() throws SQLException {
        log.info("start my first demo");
        log.info(dataSource.toString());
        Connection connection = dataSource.getConnection();
        log.info(connection.toString());

    }

    private void showdata() {
        jdbcTemplate.queryForList("select * from foo").forEach(row -> log.info(row.toString()));
    }

}
