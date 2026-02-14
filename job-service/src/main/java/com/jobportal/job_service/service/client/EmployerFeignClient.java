package com.jobportal.job_service.service.client;

import com.jobportal.job_service.dto.client.EmployerResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employer-service",fallback = EmployerFeignFallback.class)
public interface EmployerFeignClient {
    @GetMapping("employers/{id}")
    ResponseEntity<EmployerResponseDTO> getById(@PathVariable Long id);
}