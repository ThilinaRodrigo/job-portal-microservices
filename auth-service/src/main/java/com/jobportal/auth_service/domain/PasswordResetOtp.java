package com.jobportal.auth_service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_otps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String identifier; // Email or Mobile Number

    @Column(nullable = false)
    private String otpHash; // BCrypt encoded 6-digit OTP

    private String resetToken; // UUID issued upon valid OTP verification

    @Builder.Default
    private int attempts = 0; // Max 3 attempts allowed

    @Builder.Default
    private boolean verified = false;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
