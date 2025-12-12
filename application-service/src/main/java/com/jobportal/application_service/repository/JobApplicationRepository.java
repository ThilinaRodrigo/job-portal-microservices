package com.jobportal.application_service.repository;

import com.jobportal.application_service.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> getJobApplicationsByApplicantId(Long applicantId);
    List<JobApplication> getJobApplicationsByJobId(Long jobId);

    List<JobApplication> findByApplicantId(Long applicantId);
}
