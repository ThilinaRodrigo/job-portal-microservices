package com.jobportal.auth_service.service;

import com.jobportal.auth_service.dto.AuthRequest;
import com.jobportal.auth_service.dto.AuthResponse;
import com.jobportal.auth_service.dto.RegisterRequest;

public interface IAuthService {
    public AuthResponse register(RegisterRequest request);
    public AuthResponse login(AuthRequest request);
}
