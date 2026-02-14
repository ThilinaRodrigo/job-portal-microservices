package com.jobportal.application_service.service.client.fallback;

import com.jobportal.application_service.dto.client.EmployeeResponseDTO;
import com.jobportal.application_service.service.client.EmployeeFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Component
public class EmployeeFeignFallback implements EmployeeFeignClient {
    @Override
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id){
        throw new RuntimeException("Employee service is currently unavailable. Please try again later.");
    }
}
