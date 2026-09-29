package com.jobportal.gateway_server.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class RouteValidator {

    public Predicate<ServerHttpRequest> isSecured = request -> {

        String path = request.getURI().getPath();
        String method = request.getMethod().name();

        System.out.println("PATH: " + path + " METHOD: " + method);

        // Always allow CORS preflight requests
        if (method.equalsIgnoreCase("OPTIONS")) {
            return false;
        }

        if (path.startsWith("/auth") || path.startsWith("/jobportal/auth")) {
            return false;
        }

        if (method.equalsIgnoreCase("GET") && (path.contains("/jobs") || path.contains("/jobportal/jobs"))) {
            return false;
        }

        return true;
    };
}