package com.jobportal.auth_service.controller;

import com.jobportal.auth_service.dto.AuthRequest;
import com.jobportal.auth_service.dto.AuthResponse;
import com.jobportal.auth_service.dto.RegisterRequest;
import com.jobportal.auth_service.service.IAuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @GetMapping("/validate")
    public boolean validateToken(@RequestParam String token) {
        return authService.validateToken(token);
    }
}
