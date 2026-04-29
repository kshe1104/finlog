package com.finance.finlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FinlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinlogApplication.class, args);
	}

}
