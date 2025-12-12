package com.jobportal.application_service.service;

import com.jobportal.application_service.dto.JobApplicationRequestDTO;
import com.jobportal.application_service.entity.JobApplication;

import java.util.List;

public interface IJobApplicationService {

    JobApplication applyForJob(JobApplicationRequestDTO dto);
    List<JobApplication> getJobApplicationsByJobId(Long jobId);
    List<JobApplication> getJobApplicationsByApplicantId(Long applicantId);
    JobApplication getApplicationById(Long applicationId);
    JobApplication updateApplicationStatus(Long applicationId, String status);
}
