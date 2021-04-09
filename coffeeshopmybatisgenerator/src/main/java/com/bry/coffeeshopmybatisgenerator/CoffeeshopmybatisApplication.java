package com.bry.coffeeshopmybatisgenerator;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@MapperScan("com.bry.coffeeshopmybatisgenerator.mapper")
public class CoffeeshopmybatisApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(CoffeeshopmybatisApplication.class, args);
	}

	@Override public void run(ApplicationArguments args) throws Exception {
		// generateArtifacts();
	}

//	private void generateArtifacts() throws Exception {
//		log.info("generate mapper start");
//		List<String> warnings = new ArrayList<>();
//		ConfigurationParser cp = new ConfigurationParser(warnings);
//		Configuration config = cp.parseConfiguration(
//				this.getClass().getResourceAsStream("/generatorConfig.xml"));
//		DefaultShellCallback callback = new DefaultShellCallback(true);
//		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
//		myBatisGenerator.generate(null);
//		log.info("generator {}", myBatisGenerator);
//		log.info("generate mapper stop");
//	}

}
