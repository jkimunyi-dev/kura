package com.voting.kura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.voting.kura"})
public class KuraApplication {

	public static void main(String[] args) {
		SpringApplication.run(KuraApplication.class, args);
	}

}
