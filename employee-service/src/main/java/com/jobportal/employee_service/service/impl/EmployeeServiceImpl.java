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
}
