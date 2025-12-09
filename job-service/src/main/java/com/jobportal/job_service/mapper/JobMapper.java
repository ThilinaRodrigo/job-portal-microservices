package com.jobportal.job_service.mapper;

import com.jobportal.job_service.dto.JobRequestDTO;
import com.jobportal.job_service.dto.JobResponseDTO;
import com.jobportal.job_service.entity.Job;

public class JobMapper {

    public static Job reqDtoToEntity(JobRequestDTO dto) {
        return Job.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .employerId(dto.getEmployerId())
                .location(dto.getLocation())
                .type(dto.getType())
                .postedDate(dto.getPostedDate())
                .closingDate(dto.getClosingDate())
                .build();
    }

    public static JobResponseDTO entityToResDto(Job job) {
        return JobResponseDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .employerId(job.getEmployerId())
                .location(job.getLocation())
                .type(job.getType())
                .postedDate(job.getPostedDate())
                .closingDate(job.getClosingDate())
                .build();
    }


}
