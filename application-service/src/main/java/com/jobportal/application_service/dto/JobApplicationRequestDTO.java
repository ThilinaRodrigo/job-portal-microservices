package com.jobportal.application_service.dto;

import com.jobportal.application_service.enums.ApplicationStatus;
import lombok.Data;

@Data
public class JobApplicationRequestDTO {
    private Long jobId;
    private Long applicantId;
    private ApplicationStatus status;
    private String resumeUrl;
    private String coverLetter;
    private String applicantName;
    private String applicantEmail;
    private String jobTitle;
    private String companyName;
}
