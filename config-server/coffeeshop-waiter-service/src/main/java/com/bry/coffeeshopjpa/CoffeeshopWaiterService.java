package com.bry.coffeeshopjpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableJpaRepositories
@EnableTransactionManagement
@EnableCaching(proxyTargetClass = true)
//@EnableAspectJAutoProxy // 可以不开启，Spring Boot 做自动配置了。
@EnableDiscoveryClient
public class CoffeeshopWaiterService {

    public static void main(String[] args) {

        SpringApplication.run(CoffeeshopWaiterService.class, args);
    }

}
