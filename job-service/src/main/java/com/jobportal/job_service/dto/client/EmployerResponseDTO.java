package com.jobportal.job_service.dto.client;

import lombok.Data;

@Data
public class EmployerResponseDTO {
    private Long employerId;
    private String employerName;
    private String employerDescription;
    private String employerLocation;
    private String employerWebsite;
    private String employerEmail;
    private String contactPerson;
    private String contactPhone;
}
