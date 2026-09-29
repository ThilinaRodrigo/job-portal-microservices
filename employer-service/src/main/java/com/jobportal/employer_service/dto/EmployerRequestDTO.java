package com.jobportal.employer_service.dto;

import lombok.*;

@Data
public class EmployerRequestDTO {

    private Long employerId;
    private String employerName;
    private String employerDescription;
    private String employerLocation;
    private String employerWebsite;
    private String employerEmail;
    private String contactPerson;
    private String contactPhone;
    private String logoUrl;

}
