package com.jobportal.employer_service.service;

import com.jobportal.employer_service.dto.EmployerRequestDTO;
import com.jobportal.employer_service.entity.Employer;

import java.util.List;

public interface IEmployerService {

    Employer createEmployer(EmployerRequestDTO employerRequestDTO);
    Employer getEmployerById(Long employerId);
    List<Employer> getAllEmployers();
    Employer updateEmployer(Long employerId, EmployerRequestDTO employerRequestDTO);
    void deleteEmployer(Long employerId);

}
