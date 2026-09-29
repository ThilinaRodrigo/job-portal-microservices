package com.jobportal.gateway_server;

import com.jobportal.gateway_server.config.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@SpringBootApplication
public class GatewayServerApplication {

	@Autowired
	private AuthenticationFilter authenticationFilter;

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}

	@Bean
	public CorsWebFilter corsWebFilter() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOrigins(List.of(
				"http://localhost:5173",
				"http://localhost:3000",
				"http://127.0.0.1:5173"
		));
		corsConfig.setMaxAge(3600L);
		corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
		corsConfig.setAllowedHeaders(List.of("*"));
		corsConfig.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);

		return new CorsWebFilter(source);
	}

	@Bean
	public RouteLocator customRoutes(RouteLocatorBuilder builder) {

		return builder.routes()

				.route(p -> p
						.path("/jobportal/employees/**")
						.filters(f -> f
								.stripPrefix(1)
								.filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
						)
						.uri("lb://EMPLOYEE-SERVICE"))

				.route(p -> p
						.path("/jobportal/employers/**")
						.filters(f -> f
								.stripPrefix(1)
								.filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
						)
						.uri("lb://EMPLOYER-SERVICE"))

				.route(p -> p
						.path("/jobportal/job-applications/**", "/jobportal/applications/**")
						.filters(f -> f
								.stripPrefix(1)
								.rewritePath("/applications/(?<segment>.*)", "/job-applications/${segment}")
								.filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
						)
						.uri("lb://APPLICATION-SERVICE"))

				.route(p -> p
						.path("/jobportal/jobs/**")
						.filters(f -> f
								.stripPrefix(1)
								.filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
						)
						.uri("lb://JOB-SERVICE"))

				.route(p -> p
						.path("/jobportal/auth/**")
						.filters(f -> f.stripPrefix(1))
						.uri("lb://AUTH-SERVICE"))

				.build();
	}
}

