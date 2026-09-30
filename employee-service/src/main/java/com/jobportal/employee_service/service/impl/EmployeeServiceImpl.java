package com.jobportal.employee_service.service.impl;

import com.jobportal.employee_service.dto.EmployeeRequestDTO;
import com.jobportal.employee_service.entity.Employee;
import com.jobportal.employee_service.mapper.EmployeeMapper;
import com.jobportal.employee_service.repository.EmployeeRepository;
import com.jobportal.employee_service.exception.ResourceNotFoundException;
import com.jobportal.employee_service.service.IEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements IEmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee createEmployee(EmployeeRequestDTO dto) {

        Employee newEmployee = EmployeeMapper.toEntity(dto);
        employeeRepository.save(newEmployee);
        newEmployee.setId(newEmployee.getId());

        return newEmployee;
    }

    @Override
    public Employee updateEmployee(Long Id, EmployeeRequestDTO dto) {

        Employee existingEmployee = employeeRepository.findById(Id)
                .orElseGet(() -> {
                    Employee newEmp = new Employee();
                    newEmp.setId(Id);
                    return newEmp;
                });

        if (dto.getFirstName() != null) existingEmployee.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) existingEmployee.setLastName(dto.getLastName());
        if (dto.getEmail() != null) existingEmployee.setEmail(dto.getEmail());
        if (dto.getPhone() != null) existingEmployee.setPhone(dto.getPhone());
        if (dto.getSkillSet() != null) existingEmployee.setSkillSet(dto.getSkillSet());
        if (dto.getResumeLink() != null) existingEmployee.setResumeLink(dto.getResumeLink());
        if (dto.getProfilePictureUrl() != null) existingEmployee.setProfilePictureUrl(dto.getProfilePictureUrl());
        if (dto.getBio() != null) existingEmployee.setBio(dto.getBio());
        if (dto.getLocation() != null) existingEmployee.setLocation(dto.getLocation());
        if (dto.getEducation() != null) existingEmployee.setEducation(dto.getEducation());
        if (dto.getExperience() != null) existingEmployee.setExperience(dto.getExperience());

        employeeRepository.save(existingEmployee);

        return existingEmployee;
    }

    @Override
    public Employee getEmployeeById(Long Id) {

        return employeeRepository.findById(Id)
                .orElseThrow(()->new ResourceNotFoundException("Employee not found"));
    }

    @Override
    public void deleteEmployee(Long Id) {

        Employee existingEmployee = employeeRepository.findById(Id)
                .orElseThrow(()->new ResourceNotFoundException("Employee not found"));
        employeeRepository.delete(existingEmployee);
    }

    @Override
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public String uploadResume(Long id, org.springframework.web.multipart.MultipartFile file) {
        try {
            Employee employee = employeeRepository.findById(id)
                    .orElseGet(() -> {
                        Employee newEmp = new Employee();
                        newEmp.setId(id);
                        return newEmp;
                    });
            String fileName = "cv_" + id + "_" + file.getOriginalFilename();
            java.nio.file.Path uploadPath = java.nio.file.Paths.get("uploads", "resumes");
            if (!java.nio.file.Files.exists(uploadPath)) {
                java.nio.file.Files.createDirectories(uploadPath);
            }
            java.nio.file.Path filePath = uploadPath.resolve(fileName);
            java.nio.file.Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            String fileUrl = "http://localhost:8081/uploads/resumes/" + fileName;
            employee.setResumeLink(fileUrl);
            employeeRepository.save(employee);
            return fileUrl;
        } catch (java.io.IOException e) {
            throw new RuntimeException("Could not store resume file. Error: " + e.getMessage());
        }
    }

    @Override
    public String uploadPhoto(Long id, org.springframework.web.multipart.MultipartFile file) {
        try {
            Employee employee = employeeRepository.findById(id)
                    .orElseGet(() -> {
                        Employee newEmp = new Employee();
                        newEmp.setId(id);
                        return newEmp;
                    });
            String fileName = "photo_" + id + "_" + file.getOriginalFilename();
            java.nio.file.Path uploadPath = java.nio.file.Paths.get("uploads", "photos");
            if (!java.nio.file.Files.exists(uploadPath)) {
                java.nio.file.Files.createDirectories(uploadPath);
            }
            java.nio.file.Path filePath = uploadPath.resolve(fileName);
            java.nio.file.Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            String fileUrl = "http://localhost:8081/uploads/photos/" + fileName;
            employee.setProfilePictureUrl(fileUrl);
            employeeRepository.save(employee);
            return fileUrl;
        } catch (java.io.IOException e) {
            throw new RuntimeException("Could not store photo file. Error: " + e.getMessage());
        }
    }
}
