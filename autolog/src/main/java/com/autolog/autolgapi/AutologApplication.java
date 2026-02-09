package com.autolog.autolgapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.autolog")
public class AutologApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutologApplication.class, args);
	}

}
