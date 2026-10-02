package com.jobportal.application_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@EnableFeignClients
public class ApplicationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner dropCheckConstraint(JdbcTemplate jdbcTemplate) {
		return args -> {
			try {
				jdbcTemplate.execute("ALTER TABLE job_application DROP CONSTRAINT IF EXISTS job_application_status_check");
				System.out.println("Successfully removed database check constraint job_application_status_check.");
			} catch (Exception e) {
				System.err.println("Could not drop check constraint: " + e.getMessage());
			}
		};
	}

}
