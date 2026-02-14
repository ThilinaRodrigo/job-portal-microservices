package com.jobportal.application_service.service.client.fallback;

import com.jobportal.application_service.dto.client.EmployerResponseDTO;
import com.jobportal.application_service.service.client.EmployerFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class EmployerFeignFallback implements EmployerFeignClient {
    @Override
    public ResponseEntity<EmployerResponseDTO> getById(Long id) {
        throw new RuntimeException("Employer service is currently unavailable. Please try again later.");
    }
}
