package com.jobportal.employer_service.mapper;

import com.jobportal.employer_service.dto.EmployerRequestDTO;
import com.jobportal.employer_service.entity.Employer;

public class EmployerMapper {

    public static Employer toEntity(EmployerRequestDTO dto) {

        Employer employer = new Employer();

        employer.setUserId(dto.getUserId());
        employer.setEmployerName(dto.getEmployerName());
        employer.setEmployerDescription(dto.getEmployerDescription());
        employer.setEmployerLocation(dto.getEmployerLocation());
        employer.setEmployerWebsite(dto.getEmployerWebsite());
        employer.setEmployerEmail(dto.getEmployerEmail());
        employer.setContactPerson(dto.getContactPerson());
        employer.setContactPhone(dto.getContactPhone());

        return employer;
    }
}
