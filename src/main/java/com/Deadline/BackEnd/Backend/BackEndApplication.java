package com.Deadline.BackEnd.Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("file:${src/main/resources}./env")
public class BackEndApplication {
	public static void main(String[] args) {

		SpringApplication.run(BackEndApplication.class, args);
	}

}
