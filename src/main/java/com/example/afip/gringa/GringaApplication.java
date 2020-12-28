package com.example.afip.gringa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
@PropertySource("classpath:local-sensitive.conf")
public class GringaApplication {

	//TODO Create dir gringa/src/resources/download/, gringa/src/resources/local-sensitive.conf, gringa/src/resources/user.xlsx
	public static void main(String[] args) {
		SpringApplication.run(GringaApplication.class, args);
	}

}
