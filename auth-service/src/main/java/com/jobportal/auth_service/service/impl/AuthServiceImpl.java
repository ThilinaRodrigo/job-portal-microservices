package com.jobportal.auth_service.service.impl;

import com.jobportal.auth_service.config.jwt.JwtUtils;
import com.jobportal.auth_service.domain.User;
import com.jobportal.auth_service.dto.AuthRequest;
import com.jobportal.auth_service.dto.AuthResponse;
import com.jobportal.auth_service.dto.RegisterRequest;
import com.jobportal.auth_service.exception.InvalidCredentialsException;
import com.jobportal.auth_service.exception.UserAlreadyExistsException;
import com.jobportal.auth_service.repository.UserRepository;
import com.jobportal.auth_service.service.IAuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    // Registration
    public AuthResponse register(RegisterRequest request) {

        if(userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Email already in use");
        }
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .build();
        userRepository.save(user);

        String token = jwtUtils.generateToken(user);
        return new AuthResponse(token,user.getId());
    }

    // Login
    public AuthResponse login(AuthRequest request){
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtUtils.generateToken(user);
        return new AuthResponse(token,user.getId());
    }
}
