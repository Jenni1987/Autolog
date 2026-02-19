package com.autolog.autolgapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.autolog.repository")
@EntityScan(basePackages = "com.autolog.model")
@ComponentScan(basePackages="com.autolog")
public class AutologApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutologApplication.class, args);
	}

}
