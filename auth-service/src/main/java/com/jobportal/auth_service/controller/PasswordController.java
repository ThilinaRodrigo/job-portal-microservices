package com.jobportal.auth_service.controller;

import com.jobportal.auth_service.dto.*;
import com.jobportal.auth_service.service.IPasswordManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class PasswordController {

    private final IPasswordManagementService passwordService;

    @PostMapping("/forgot-password/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(@Valid @RequestBody SendOtpRequestDTO dto) {
        return ResponseEntity.ok(passwordService.sendForgotPasswordOtp(dto));
    }

    @PostMapping("/forgot-password/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@Valid @RequestBody VerifyOtpRequestDTO dto) {
        return ResponseEntity.ok(passwordService.verifyOtp(dto));
    }

    @PostMapping("/forgot-password/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO dto) {
        return ResponseEntity.ok(passwordService.resetPassword(dto));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestHeader(value = "X-User-Email", required = false) String email,
            @Valid @RequestBody ChangePasswordRequestDTO dto
    ) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "User authentication details missing."));
        }
        return ResponseEntity.ok(passwordService.changePassword(email, dto));
    }
}
