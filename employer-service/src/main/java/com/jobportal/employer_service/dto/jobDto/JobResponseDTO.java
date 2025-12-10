package com.jobportal.employer_service.dto.jobDto;

import com.jobportal.employer_service.enums.JobType;

import lombok.Data;

import java.time.LocalDate;

@Data
public class JobResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Long employerId;
    private String location;

    private JobType type;

    private LocalDate postedDate;
    private LocalDate closingDate;
}
