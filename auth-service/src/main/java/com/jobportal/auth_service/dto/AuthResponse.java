package com.jobportal.auth_service.dto;

import com.jobportal.auth_service.domain.Role;

public record AuthResponse(
        String token,
        long id,
        String email,
        Role role
) { }

