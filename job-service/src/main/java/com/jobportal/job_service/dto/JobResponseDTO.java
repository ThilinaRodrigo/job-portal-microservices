package com.jobportal.job_service.dto;

import com.jobportal.job_service.enums.JobType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobResponseDTO {

    private Long id;
    private String title;
    private String description;
    private Long employerId;
    private String location;

    @Enumerated(EnumType.STRING)
    private JobType type; // e.g., Full-time, Part-time, Contract

    private LocalDate postedDate;
    private LocalDate closingDate;
}
