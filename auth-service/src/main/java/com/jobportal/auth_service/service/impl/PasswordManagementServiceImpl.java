package com.jobportal.auth_service.service.impl;

import com.jobportal.auth_service.domain.PasswordResetOtp;
import com.jobportal.auth_service.domain.User;
import com.jobportal.auth_service.dto.*;
import com.jobportal.auth_service.exception.ResourceNotFoundException;
import com.jobportal.auth_service.repository.PasswordResetOtpRepository;
import com.jobportal.auth_service.repository.UserRepository;
import com.jobportal.auth_service.service.IPasswordManagementService;
import com.jobportal.auth_service.service.ISmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordManagementServiceImpl implements IPasswordManagementService {

    private final UserRepository userRepository;
    private final PasswordResetOtpRepository otpRepository;
    private final ISmsService smsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Map<String, String> sendForgotPasswordOtp(SendOtpRequestDTO dto) {
        String input = dto.getIdentifier() != null ? dto.getIdentifier().trim() : "";
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Phone number or email is required.");
        }

        // Find user by Email or Phone
        User user = userRepository.findByEmail(input)
                .orElseGet(() -> userRepository.findByPhone(input)
                        .orElseThrow(() -> new ResourceNotFoundException("No account registered with " + input)));

        // Generate 6-digit OTP
        String rawOtp = String.format("%06d", new SecureRandom().nextInt(1000000));
        String hashedOtp = passwordEncoder.encode(rawOtp);

        // Delete existing unverified OTPs for this identifier
        try {
            otpRepository.deleteByIdentifier(input);
        } catch (Exception ignored) {}

        PasswordResetOtp otpEntity = PasswordResetOtp.builder()
                .user(user)
                .identifier(input)
                .otpHash(hashedOtp)
                .attempts(0)
                .verified(false)
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .createdAt(LocalDateTime.now())
                .build();

        otpRepository.save(otpEntity);

        // Determine target phone number for Textit.biz SMS dispatch
        String recipientPhone = user.getPhone() != null && !user.getPhone().isEmpty() ? user.getPhone() : input;
        boolean isPhone = recipientPhone.matches(".*\\d.*");

        if (isPhone) {
            String smsMessage = "Your JobFinder verification code is: " + rawOtp + ". Valid for 5 minutes. Do not share with anyone.";
            boolean smsSent = smsService.sendSms(recipientPhone, smsMessage);
            log.info("SMS Dispatch Status for {}: {}", recipientPhone, smsSent);
        }

        log.info("Generated OTP for user {}: {}", user.getEmail(), rawOtp);

        String maskedRecipient = isPhone && recipientPhone.length() >= 7
                ? recipientPhone.substring(0, 3) + "****" + recipientPhone.substring(recipientPhone.length() - 3)
                : user.getEmail();

        return Map.of(
                "message", "Verification OTP sent successfully.",
                "recipient", maskedRecipient,
                "identifier", input
        );
    }

    @Override
    @Transactional
    public Map<String, String> verifyOtp(VerifyOtpRequestDTO dto) {
        String input = dto.getIdentifier() != null ? dto.getIdentifier().trim() : "";
        String inputOtp = dto.getOtp() != null ? dto.getOtp().trim() : "";

        PasswordResetOtp otpRecord = otpRepository.findTopByIdentifierAndVerifiedFalseOrderByCreatedAtDesc(input)
                .orElseThrow(() -> new IllegalArgumentException("No active OTP request found for " + input + ". Please request a new OTP."));

        if (otpRecord.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Verification OTP has expired. Please request a new OTP.");
        }

        if (otpRecord.getAttempts() >= 3) {
            throw new IllegalArgumentException("Maximum verification attempts exceeded. Please request a new OTP.");
        }

        // Increment attempts
        otpRecord.setAttempts(otpRecord.getAttempts() + 1);

        if (!passwordEncoder.matches(inputOtp, otpRecord.getOtpHash())) {
            otpRepository.save(otpRecord);
            int remaining = 3 - otpRecord.getAttempts();
            throw new IllegalArgumentException("Invalid OTP code. " + (remaining > 0 ? remaining + " attempt(s) remaining." : "Please request a new OTP."));
        }

        // OTP Verified successfully
        String resetToken = UUID.randomUUID().toString();
        otpRecord.setVerified(true);
        otpRecord.setResetToken(resetToken);
        otpRepository.save(otpRecord);

        return Map.of(
                "message", "OTP verified successfully.",
                "resetToken", resetToken
        );
    }

    @Override
    @Transactional
    public Map<String, String> resetPassword(ResetPasswordRequestDTO dto) {
        String token = dto.getResetToken() != null ? dto.getResetToken().trim() : "";
        String newPass = dto.getNewPassword() != null ? dto.getNewPassword().trim() : "";

        PasswordResetOtp otpRecord = otpRepository.findByResetTokenAndVerifiedTrue(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired password reset session. Please restart the process."));

        if (otpRecord.getCreatedAt().plusMinutes(15).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reset session expired. Please request a new OTP.");
        }

        User user = otpRecord.getUser();
        user.setPassword(passwordEncoder.encode(newPass));
        userRepository.save(user);

        // Clean up OTP record
        try {
            otpRepository.delete(otpRecord);
        } catch (Exception ignored) {}

        log.info("Password updated successfully for user ID {}", user.getId());

        return Map.of("message", "Password reset successfully. Please log in with your new password.");
    }

    @Override
    @Transactional
    public Map<String, String> changePassword(String email, ChangePasswordRequestDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password does not match.");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        return Map.of("message", "Password updated successfully.");
    }
}
