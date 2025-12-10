package com.jobportal.employer_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Employer {

        @Id
        private String employerId;

        private Long userId;
        private String employerName;
        private String employerDescription;
        private String employerLocation;
        private String employerWebsite;
        private String employerEmail;
        private String contactPerson;
        private String contactPhone;

}
