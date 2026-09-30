package com.jobportal.application_service.mapper;

import com.jobportal.application_service.dto.JobApplicationRequestDTO;
import com.jobportal.application_service.entity.JobApplication;

import java.time.LocalDate;

public class JobApplicationMapper {

    public static JobApplication toEntity(JobApplicationRequestDTO dto) {
        JobApplication entity = new JobApplication();
        entity.setJobId(dto.getJobId());
        entity.setApplicantId(dto.getApplicantId());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : com.jobportal.application_service.enums.ApplicationStatus.PENDING);
        entity.setAppliedDate(LocalDate.now());
        entity.setResumeUrl(dto.getResumeUrl());
        entity.setCoverLetter(dto.getCoverLetter());
        entity.setApplicantName(dto.getApplicantName());
        entity.setApplicantEmail(dto.getApplicantEmail());
        entity.setJobTitle(dto.getJobTitle());
        entity.setCompanyName(dto.getCompanyName());
        return entity;
    }
}
