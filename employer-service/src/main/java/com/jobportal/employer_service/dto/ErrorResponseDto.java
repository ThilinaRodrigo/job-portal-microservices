package com.jobportal.employer_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseDto {
    String apiPath;
    String errorMsg;
    HttpStatus errorCode;
    LocalDateTime timestamp;
}
