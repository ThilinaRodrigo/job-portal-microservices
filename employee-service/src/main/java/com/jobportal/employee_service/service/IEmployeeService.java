package com.jobportal.employee_service.service;

import com.jobportal.employee_service.dto.EmployeeRequestDTO;
import com.jobportal.employee_service.entity.Employee;

import java.util.List;

public interface IEmployeeService {

    Employee createEmployee(EmployeeRequestDTO dto);
    Employee updateEmployee(Long Id,EmployeeRequestDTO dto);
    Employee getEmployeeById(Long Id);
    void deleteEmployee(Long Id);
    List<Employee> getEmployees();
}
