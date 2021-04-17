package com.example.autoconfigdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.autoconfighello.Greeting;

@SpringBootApplication
public class AutoconfigDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutoconfigDemoApplication.class, args);
	}

	//@Bean
	public Greeting greeting(){
		return new Greeting("from created bean in appplication");
	}
}
