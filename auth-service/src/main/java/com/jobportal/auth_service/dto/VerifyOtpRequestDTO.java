package com.jobportal.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyOtpRequestDTO {
    @NotBlank(message = "Identifier is required")
    private String identifier;

    @NotBlank(message = "OTP code is required")
    private String otp;
}
