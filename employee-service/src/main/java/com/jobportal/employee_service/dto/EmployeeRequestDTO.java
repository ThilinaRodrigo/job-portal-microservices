package com.jobportal.employee_service.dto;

import lombok.Data;

@Data
public class EmployeeRequestDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String skillSet;
    private String resumeLink;
}
