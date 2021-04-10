package com.bry.nosqldemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class NosqldemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(NosqldemoApplication.class, args);
	}

}
