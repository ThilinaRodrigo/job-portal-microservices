package com.jobportal.job_service.controller;

import com.jobportal.job_service.dto.JobRequestDTO;
import com.jobportal.job_service.dto.JobResponseDTO;
import com.jobportal.job_service.service.impl.JobServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobServiceImpl jobService;

    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping("/create")
    public ResponseEntity<JobResponseDTO> createJob(@RequestBody JobRequestDTO jobRequestDTO) {
        JobResponseDTO createdJob = jobService.createJob(jobRequestDTO);
        return ResponseEntity.ok(createdJob);
    }


    @GetMapping("/employer/{employerId}")
    public ResponseEntity<List<JobResponseDTO>> getJobsByEmployer(@PathVariable Long employerId) {
        java.util.List<JobResponseDTO> jobs = jobService.getJobsByEmployer(employerId);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponseDTO> getJobById(@PathVariable Long jobId) {
        JobResponseDTO job = jobService.getJobById(jobId);
        return ResponseEntity.ok(job);
    }

    @GetMapping
    public ResponseEntity<List<JobResponseDTO>> getAllJobs() {
        List<JobResponseDTO> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @PutMapping("/{jobId}")
    public ResponseEntity<JobResponseDTO> updateJob(@PathVariable Long jobId, @RequestBody JobRequestDTO jobRequestDTO) {
        JobResponseDTO updatedJob = jobService.updateJob(jobId, jobRequestDTO);
        return ResponseEntity.ok(updatedJob);
    }
}
