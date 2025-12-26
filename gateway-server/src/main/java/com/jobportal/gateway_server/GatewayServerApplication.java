package com.jobportal.gateway_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}

	@Bean
	public RouteLocator customRoutes(RouteLocatorBuilder builder) {
		return builder.routes()

				.route(p -> p
						.path("/jobportal/accounts/**")
						.filters(f -> f.stripPrefix(1))
						.uri("lb://EMPLOYEE-SERVICE"))

				.route(p -> p
						.path("/jobportal/cards/**")
						.filters(f -> f.stripPrefix(1))
						.uri("lb://EMPLOYER-SERVICE"))

				.route(p -> p
						.path("/jobportal/applications/**")
						.filters(f -> f.stripPrefix(1))
						.uri("lb://APPLICATION-SERVICE"))

				.route(p -> p
						.path("/jobportal/jobs/**")
						.filters(f -> f.stripPrefix(1))
						.uri("lb://JOB-SERVICE"))

				.build();
	}
}
