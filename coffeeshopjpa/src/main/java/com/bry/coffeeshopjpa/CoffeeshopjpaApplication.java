package com.bry.coffeeshopjpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableJpaRepositories
@EnableTransactionManagement
@EnableCaching(proxyTargetClass = true)
//@EnableAspectJAutoProxy // 可以不开启，Spring Boot 做自动配置了。
public class CoffeeshopjpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffeeshopjpaApplication.class, args);
    }

}
