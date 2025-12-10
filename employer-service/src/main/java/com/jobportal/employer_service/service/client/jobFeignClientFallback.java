package com.jobportal.employer_service.service.client;

import com.jobportal.employer_service.dto.jobDto.JobRequestDTO;
import com.jobportal.employer_service.dto.jobDto.JobResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class jobFeignClientFallback implements JobFeignClient {

    @Override
    public ResponseEntity<JobResponseDTO> createJob(JobRequestDTO jobRequestDTO) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}
