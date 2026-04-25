package com.jobportal.gateway_server.config;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtils jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }


    private final Map<String, List<RouteRule>> ROLE_ACCESS = Map.of(

            "ADMIN", List.of(
                    new RouteRule("GET", "/employees")
            ),

            "EMPLOYER", List.of(
                    // Employer management
                    new RouteRule("POST", "/employers"),
                    new RouteRule("GET", "/employers"),
                    new RouteRule("PUT", "/employers"),
                    new RouteRule("DELETE", "/employers"),

                    // Job management
                    new RouteRule("POST", "/jobs"),     // create job
                    new RouteRule("PUT", "/jobs"),      // update job

                    // Applications
                    new RouteRule("GET", "/job-applications/job"),
                    new RouteRule("PUT", "/job-applications") // update status
            ),

            "EMPLOYEE", List.of(
                    // Employee profile
                    new RouteRule("POST", "/employees"),
                    new RouteRule("GET", "/employees"),
                    new RouteRule("PUT", "/employees"),
                    new RouteRule("DELETE", "/employees"),

                    // Job applications
                    new RouteRule("POST", "/job-applications"),
                    new RouteRule("GET", "/job-applications/applicant"),

                    new RouteRule("GET", "/jobs")
            )
    );

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            System.out.println("Authentication filter running...");

            if (validator.isSecured.test(exchange.getRequest())) {

                String authHeader = exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);

                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    return onError(exchange, "Missing or Invalid Authorization Header", HttpStatus.UNAUTHORIZED);
                }

                String token = authHeader.substring(7);

                try {

                    Claims claims = jwtUtil.validateAndExtract(token);

                    String role = claims.get("role", String.class);
                    String email = claims.getSubject();
                    String path = exchange.getRequest().getURI().getPath();
                    String method = exchange.getRequest().getMethod().name();

                    System.out.println("ROLE: " + role + " PATH: " + path + " METHOD: " + method);

                    boolean allowed = false;

                    if (ROLE_ACCESS.containsKey(role)) {
                        for (RouteRule rule : ROLE_ACCESS.get(role)) {

                            boolean pathMatch = path.startsWith(rule.getPath());

                            if (rule.getPath().equals("/jobportal/applications")
                                    && path.contains("/status")) {
                                pathMatch = true;
                            }

                            if (pathMatch && method.equalsIgnoreCase(rule.getMethod())) {
                                allowed = true;
                                break;
                            }
                        }
                    }

                    if (!allowed) {
                        return onError(exchange, "Forbidden - Access Denied", HttpStatus.FORBIDDEN);
                    }

                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header("X-User-Email", email)
                            .header("X-User-Role", role)
                            .build();

                    return chain.filter(exchange.mutate().request(request).build());

                } catch (Exception e) {
                    return onError(exchange, "Invalid Token", HttpStatus.UNAUTHORIZED);
                }
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        String body = "{ \"error\": \"" + err + "\" }";

        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(body.getBytes()))
        );
    }

    public static class Config {}


    public static class RouteRule {
        private final String method;
        private final String path;

        public RouteRule(String method, String path) {
            this.method = method;
            this.path = path;
        }

        public String getMethod() {
            return method;
        }

        public String getPath() {
            return path;
        }
    }
}