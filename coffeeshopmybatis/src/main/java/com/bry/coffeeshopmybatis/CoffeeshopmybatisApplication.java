package com.bry.coffeeshopmybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.bry.coffeeshopmybatis.mapper")
public class CoffeeshopmybatisApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoffeeshopmybatisApplication.class, args);
	}

}
