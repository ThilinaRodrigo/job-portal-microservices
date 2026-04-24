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

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtils jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            System.out.println("Authentication filter running....");
            if (validator.isSecured.test(exchange.getRequest())) {

                if (!exchange.getRequest().getHeaders().containsHeader(HttpHeaders.AUTHORIZATION)) {
                    return onError(exchange, "Missing token", HttpStatus.UNAUTHORIZED);
                }

                String authHeader = exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);

                String token = authHeader.substring(7);

                try {
                    Claims claims = jwtUtil.validateAndExtract(token);

                    // Forward user info
                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header("X-User-Email", claims.getSubject())
                            .header("X-User-Role", claims.get("role", String.class))
                            .build();

                    return chain.filter(exchange.mutate().request(request).build());

                } catch (Exception e) {
                    return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
                }
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    public static class Config {}
}
