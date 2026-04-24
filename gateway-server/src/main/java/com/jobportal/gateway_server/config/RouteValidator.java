package com.jobportal.gateway_server.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/auth/login",
            "/auth/register",
            "/eureka",
            "/jobportal/jobs"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> {

                String path = request.getURI().getPath();
                String method = request.getMethod().name();

                System.out.println("PATH: " + path + " METHOD: " + method);


                if (path.startsWith("/auth")) {
                    return false;
                }

                if (method.equalsIgnoreCase("GET") && path.contains("/jobs")) {
                    return false;
                }

                return true;
            };
}
