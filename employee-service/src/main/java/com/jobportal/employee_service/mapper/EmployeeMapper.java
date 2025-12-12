package com.jobportal.employee_service.mapper;

import com.jobportal.employee_service.dto.EmployeeRequestDTO;
import com.jobportal.employee_service.entity.Employee;

public class EmployeeMapper {

    public static Employee toEntity(EmployeeRequestDTO dto){
        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setSkillSet(dto.getSkillSet());
        employee.setResumeLink(dto.getResumeLink());
        return employee;
    }
}
