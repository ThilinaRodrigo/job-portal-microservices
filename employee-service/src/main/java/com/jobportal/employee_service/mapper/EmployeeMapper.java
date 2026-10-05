package com.jobportal.employee_service.mapper;

import com.jobportal.employee_service.dto.EmployeeRequestDTO;
import com.jobportal.employee_service.entity.Employee;

public class EmployeeMapper {

    public static Employee toEntity(EmployeeRequestDTO dto){
        Employee employee = new Employee();
        if (dto.getId() != null) {
            employee.setId(dto.getId());
        }
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setSkillSet(dto.getSkillSet());
        employee.setResumeLink(dto.getResumeLink());
        employee.setProfilePictureUrl(dto.getProfilePictureUrl());
        employee.setBio(dto.getBio());
        employee.setLocation(dto.getLocation());
        employee.setEducation(dto.getEducation());
        employee.setExperience(dto.getExperience());
        return employee;
    }
}
