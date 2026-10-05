package com.jobportal.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendOtpRequestDTO {
    @NotBlank(message = "Identifier (Email or Phone Number) is required")
    private String identifier;
}
