package com.jobportal.employer_service.service.impl;

import com.jobportal.employer_service.dto.EmployerRequestDTO;
import com.jobportal.employer_service.entity.Employer;
import com.jobportal.employer_service.mapper.EmployerMapper;
import com.jobportal.employer_service.repository.EmployerRepository;
import com.jobportal.employer_service.service.IEmployerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployerServiceImpl implements IEmployerService {

    private final EmployerRepository employerRepository;

    @Override
    public Employer createEmployer(EmployerRequestDTO employerRequestDTO) {

//        if(employerRepository.findByUserId(employerRequestDTO.getUserId()) != null) {
//            throw new RuntimeException("Employer with userId " + employerRequestDTO.getUserId() + " already exists.");
//        }

        Employer employer = EmployerMapper.toEntity(employerRequestDTO);
        employerRepository.save(employer);
        employer.setEmployerId(employer.getEmployerId());

        return employer;
    }

    @Override
    public Employer getEmployerById(Long employerId) {
        return employerRepository.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Employer with id " + employerId + " not found."));
    }

    @Override
    public List<Employer> getAllEmployers() {
        return employerRepository.findAll();
    }

    @Override
    public Employer updateEmployer(Long employerId, EmployerRequestDTO employerRequestDTO) {

        Employer existingEmployer = employerRepository.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Employer with id " + employerId + " not found."));
        Employer updatedEmployer = EmployerMapper.toEntity(employerRequestDTO);
        updatedEmployer.setEmployerId(existingEmployer.getEmployerId());
        employerRepository.save(updatedEmployer);

        return updatedEmployer;
    }

    @Override
    public void deleteEmployer(Long employerId) {

        if (employerRepository.existsById(employerId)) {
            employerRepository.deleteById(employerId);
        } else {
            throw new RuntimeException("Employer with id " + employerId + " not found.");
        }

    }
}
