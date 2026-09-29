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
        Employer employer = EmployerMapper.toEntity(employerRequestDTO);
        return employerRepository.save(employer);
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
        Employer employer = employerRepository.findById(employerId)
                .orElseGet(() -> {
                    Employer newEmp = new Employer();
                    newEmp.setEmployerId(employerId);
                    return newEmp;
                });

        if (employerRequestDTO.getEmployerName() != null) employer.setEmployerName(employerRequestDTO.getEmployerName());
        if (employerRequestDTO.getEmployerDescription() != null) employer.setEmployerDescription(employerRequestDTO.getEmployerDescription());
        if (employerRequestDTO.getEmployerLocation() != null) employer.setEmployerLocation(employerRequestDTO.getEmployerLocation());
        if (employerRequestDTO.getEmployerWebsite() != null) employer.setEmployerWebsite(employerRequestDTO.getEmployerWebsite());
        if (employerRequestDTO.getEmployerEmail() != null) employer.setEmployerEmail(employerRequestDTO.getEmployerEmail());
        if (employerRequestDTO.getContactPerson() != null) employer.setContactPerson(employerRequestDTO.getContactPerson());
        if (employerRequestDTO.getContactPhone() != null) employer.setContactPhone(employerRequestDTO.getContactPhone());
        if (employerRequestDTO.getLogoUrl() != null) employer.setLogoUrl(employerRequestDTO.getLogoUrl());

        return employerRepository.save(employer);
    }

    @Override
    public void deleteEmployer(Long employerId) {
        if (employerRepository.existsById(employerId)) {
            employerRepository.deleteById(employerId);
        } else {
            throw new RuntimeException("Employer with id " + employerId + " not found.");
        }
    }

    @Override
    public String uploadLogo(Long employerId, org.springframework.web.multipart.MultipartFile file) {
        try {
            Employer employer = employerRepository.findById(employerId)
                    .orElseGet(() -> {
                        Employer newEmp = new Employer();
                        newEmp.setEmployerId(employerId);
                        return newEmp;
                    });
            String fileName = "employer_" + employerId + "_" + file.getOriginalFilename();
            java.nio.file.Path uploadPath = java.nio.file.Paths.get("uploads", "employers");
            if (!java.nio.file.Files.exists(uploadPath)) {
                java.nio.file.Files.createDirectories(uploadPath);
            }
            java.nio.file.Path filePath = uploadPath.resolve(fileName);
            java.nio.file.Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            String fileUrl = "http://localhost:8083/uploads/employers/" + fileName;
            employer.setLogoUrl(fileUrl);
            employerRepository.save(employer);
            return fileUrl;
        } catch (java.io.IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }
}
