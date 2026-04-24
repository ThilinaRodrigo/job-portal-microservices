package com.jobportal.employee_service.controller;

import com.jobportal.employee_service.dto.EmployeeRequestDTO;
import com.jobportal.employee_service.entity.Employee;
import com.jobportal.employee_service.service.impl.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeServiceImpl service;

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody EmployeeRequestDTO dto) {
        Employee createdEmployee = service.createEmployee(dto);
        return ResponseEntity.ok(createdEmployee);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = service.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequestDTO dto) {
        Employee updatedEmployee = service.updateEmployee(id, dto);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        service.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping
    public ResponseEntity<java.util.List<Employee>> getAllEmployees() {
        List<Employee> employees = service.getEmployees();
        return ResponseEntity.ok(employees);
    }
}
