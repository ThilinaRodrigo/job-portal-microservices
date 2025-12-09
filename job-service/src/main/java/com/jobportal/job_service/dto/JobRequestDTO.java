package com.jobportal.job_service.dto;

import com.jobportal.job_service.enums.JobType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobRequestDTO {

    private String title;
    private String description;
    private Long employerId;
    private String location;
    private JobType type;
    private LocalDate postedDate;
    private LocalDate closingDate;
}
