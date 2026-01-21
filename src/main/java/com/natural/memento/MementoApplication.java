package com.natural.memento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class MementoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MementoApplication.class, args);
	}
}
