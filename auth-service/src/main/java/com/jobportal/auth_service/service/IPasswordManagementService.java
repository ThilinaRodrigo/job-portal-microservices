package com.jobportal.auth_service.service;

import com.jobportal.auth_service.dto.*;

import java.util.Map;

public interface IPasswordManagementService {

    Map<String, String> sendForgotPasswordOtp(SendOtpRequestDTO dto);

    Map<String, String> verifyOtp(VerifyOtpRequestDTO dto);

    Map<String, String> resetPassword(ResetPasswordRequestDTO dto);

    Map<String, String> changePassword(String email, ChangePasswordRequestDTO dto);
}
