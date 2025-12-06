package com.jobportal.auth_service.config.jwt;

import com.jobportal.auth_service.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expirationMs;

    public String generateToken(User user){
        return Jwts.builder()
                .setSubject((user.getEmail()))
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date((System.currentTimeMillis() + expirationMs)))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

//    public String extractEmail(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(secret.getBytes())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
}
