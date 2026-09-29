package com.jobportal.employer_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Employer {

    @Id
    private Long employerId;

    private String employerName;

    @Column(columnDefinition = "TEXT")
    private String employerDescription;

    private String employerLocation;
    private String employerWebsite;
    private String employerEmail;
    private String contactPerson;
    private String contactPhone;

    @Column(columnDefinition = "TEXT")
    private String logoUrl;

}
