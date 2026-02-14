package com.jobportal.application_service.service.client.fallback;

import com.jobportal.application_service.dto.client.JobResponseDTO;
import com.jobportal.application_service.service.client.JobFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class JobFeignFallback implements JobFeignClient {
    @Override
    public ResponseEntity<JobResponseDTO> getJobById(Long jobId) {
       throw new RuntimeException("Job not found");
    }
}
