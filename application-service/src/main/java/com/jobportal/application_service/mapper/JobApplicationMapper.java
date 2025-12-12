package com.jobportal.application_service.mapper;

import com.jobportal.application_service.dto.JobApplicationRequestDTO;
import com.jobportal.application_service.entity.JobApplication;

import java.time.LocalDate;

public class JobApplicationMapper {

    public static JobApplication toEntity(JobApplicationRequestDTO dto) {
        JobApplication entity = new JobApplication();
        entity.setJobId(dto.getJobId());
        entity.setApplicantId(dto.getApplicantId());
        entity.setStatus(dto.getStatus());
        entity.setAppliedDate(LocalDate.now());
        return entity;
    }
}
