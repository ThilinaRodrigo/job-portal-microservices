package com.jobportal.auth_service.dto;

public record AuthResponse(
        String token,
        long id
) { }
