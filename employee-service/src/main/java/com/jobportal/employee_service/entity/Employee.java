package com.jobportal.employee_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Employee {

    @Id
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String skillSet;
    private String resumeLink;
}
