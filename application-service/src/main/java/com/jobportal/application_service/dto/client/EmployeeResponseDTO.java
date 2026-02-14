package com.jobportal.application_service.dto.client;

import lombok.Data;

@Data
public class EmployeeResponseDTO {
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String skillSet;
    private String resumeLink;
}
