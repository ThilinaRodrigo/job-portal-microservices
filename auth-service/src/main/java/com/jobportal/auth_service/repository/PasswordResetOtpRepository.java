package com.jobportal.auth_service.repository;

import com.jobportal.auth_service.domain.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {

    Optional<PasswordResetOtp> findTopByIdentifierAndVerifiedFalseOrderByCreatedAtDesc(String identifier);

    Optional<PasswordResetOtp> findByResetTokenAndVerifiedTrue(String resetToken);

    void deleteByIdentifier(String identifier);
}
