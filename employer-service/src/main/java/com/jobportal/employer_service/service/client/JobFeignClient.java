package com.jobportal.employer_service.service.client;

import com.jobportal.employer_service.dto.jobDto.JobRequestDTO;
import com.jobportal.employer_service.dto.jobDto.JobResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "job-service", fallback = jobFeignClientFallback.class)
public interface JobFeignClient {

    @PostMapping("/jobs/create")
    ResponseEntity<JobResponseDTO> createJob(@RequestBody JobRequestDTO jobRequestDTO);
}
