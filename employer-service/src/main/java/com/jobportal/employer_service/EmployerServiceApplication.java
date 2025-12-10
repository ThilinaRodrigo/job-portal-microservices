package com.jobportal.employer_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EmployerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployerServiceApplication.class, args);
	}

}
