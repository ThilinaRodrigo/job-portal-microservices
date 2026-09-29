package com.jobportal.auth_service.dto;

import com.jobportal.auth_service.domain.Role;

public record RegisterRequest(
        String email,
        String password,
        Role role,
        String phone,
        String firstName,
        String companyName
) { }
