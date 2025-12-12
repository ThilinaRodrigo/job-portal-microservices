package com.jobportal.application_service.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String apiPath,
       String errorMsg,
       HttpStatus errorCode,
       LocalDateTime timestamp) { }
