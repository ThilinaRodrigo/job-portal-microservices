package com.jobportal.job_service.entity;

import com.jobportal.job_service.enums.JobType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employerId;

    private String title;
    private String description;
    private String location;

    @Enumerated(EnumType.STRING)
    private JobType type;

    private LocalDate postedDate;
    private LocalDate closingDate;
}
