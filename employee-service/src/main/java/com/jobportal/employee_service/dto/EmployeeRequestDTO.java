package com.jobportal.employee_service.dto;

import lombok.Data;

@Data
public class EmployeeRequestDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String skillSet;
    private String resumeLink;
}
