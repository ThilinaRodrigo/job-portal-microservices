package com.jobportal.job_service.service.client;

import com.jobportal.job_service.dto.client.EmployerResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class EmployerFeignFallback implements EmployerFeignClient{
    @Override
    public ResponseEntity<EmployerResponseDTO> getById(Long id) {
        throw new RuntimeException("Employer service is currently unavailable. Please try again later.");
    }
}
