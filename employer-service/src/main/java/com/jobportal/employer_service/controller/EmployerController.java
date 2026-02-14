package com.jobportal.employer_service.controller;

import com.jobportal.employer_service.dto.EmployerRequestDTO;
import com.jobportal.employer_service.dto.jobDto.JobRequestDTO;
import com.jobportal.employer_service.dto.jobDto.JobResponseDTO;
import com.jobportal.employer_service.entity.Employer;
import com.jobportal.employer_service.service.client.JobFeignClient;
import com.jobportal.employer_service.service.impl.EmployerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employers")
@RequiredArgsConstructor
public class EmployerController {

    private final EmployerServiceImpl employerService;
    private final JobFeignClient jobFeignClient;

    @PostMapping
    public ResponseEntity<Employer> create(@RequestBody EmployerRequestDTO employerRequestDTO) {
        Employer createdEmployer = employerService.createEmployer(employerRequestDTO);
        return ResponseEntity.ok(createdEmployer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employer> getById(@PathVariable Long id) {
        Employer employer = employerService.getEmployerById(id);
        return ResponseEntity.ok(employer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employer> update(@PathVariable Long id, @RequestBody EmployerRequestDTO employerRequestDTO) {
        Employer updatedEmployer = employerService.updateEmployer(id, employerRequestDTO);
        return ResponseEntity.ok(updatedEmployer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employerService.deleteEmployer(id);
        return ResponseEntity.noContent().build();
    }

//    @PostMapping("/jobs/create")
//    public ResponseEntity<?> createJob(@RequestBody JobRequestDTO jobRequestDTO) {
//        return jobFeignClient.createJob(jobRequestDTO);
//    }
//
//    @PutMapping("jobs/{jobId}")
//    public ResponseEntity<JobResponseDTO> updateJob(@PathVariable Long jobId, @RequestBody JobRequestDTO jobRequestDTO){
//        return jobFeignClient.updateJob(jobId, jobRequestDTO);
//    }
}
