package com.jobportal.employee_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(columnDefinition = "TEXT")
    private String skillSet;

    @Column(length = 1000)
    private String resumeLink;

    @Column(length = 1000)
    private String profilePictureUrl;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String location;

    @Column(columnDefinition = "TEXT")
    private String education;

    @Column(columnDefinition = "TEXT")
    private String experience;
}
