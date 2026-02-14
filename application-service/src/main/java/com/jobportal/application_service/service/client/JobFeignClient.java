package com.jobportal.application_service.service.client;

import com.jobportal.application_service.dto.client.JobResponseDTO;
import com.jobportal.application_service.service.client.fallback.EmployeeFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "job-service",fallback = EmployeeFeignFallback.class)
public interface JobFeignClient {
    @GetMapping("jobs/{jobId}")
    ResponseEntity<JobResponseDTO> getJobById(@PathVariable Long jobId);
}
