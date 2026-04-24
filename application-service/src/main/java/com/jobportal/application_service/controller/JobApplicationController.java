package com.jobportal.application_service.controller;

import com.jobportal.application_service.dto.JobApplicationRequestDTO;
import com.jobportal.application_service.entity.JobApplication;
import com.jobportal.application_service.service.Impl.JobApplicationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/job-applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationServiceImpl jobApplicationService;

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping
    public ResponseEntity<JobApplication> applyForJob(@RequestBody  JobApplicationRequestDTO dto) {
        return ResponseEntity.ok(jobApplicationService.applyForJob(dto));
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("applicant/{applicantId}")
    public ResponseEntity<java.util.List<JobApplication>> getApplicationsByApplicantId(@PathVariable Long applicantId) {
        return ResponseEntity.ok(jobApplicationService.getJobApplicationsByApplicantId(applicantId));
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("job/{jobId}")
    public ResponseEntity<java.util.List<JobApplication>> getApplicationsByJobId(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobApplicationService.getJobApplicationsByJobId(jobId));
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<JobApplication> updateApplicationStatus(@PathVariable Long applicationId, @RequestParam String status) {
        return ResponseEntity.ok(jobApplicationService.updateApplicationStatus(applicationId, status));
    }
}
