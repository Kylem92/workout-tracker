package com.kyle.exercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@ComponentScan(basePackages = { "com.kyle.exercise" })
public class ExerciseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExerciseApplication.class, args);
	}

}
