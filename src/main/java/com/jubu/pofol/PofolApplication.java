package com.jubu.pofol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PofolApplication {

	public static void main(String[] args) {
		SpringApplication.run(PofolApplication.class, args);
	}

}
