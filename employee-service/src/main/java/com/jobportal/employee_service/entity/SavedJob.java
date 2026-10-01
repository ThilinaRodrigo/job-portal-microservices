package com.jobportal.employee_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "saved_jobs", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"employeeId", "jobId"})
})
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class SavedJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;
    private Long jobId;

    private String jobTitle;
    private String companyName;
    private String location;
    private String salary;
    private String type;

    @Column(length = 1000)
    private String logoUrl;

    private LocalDate savedDate;
}
