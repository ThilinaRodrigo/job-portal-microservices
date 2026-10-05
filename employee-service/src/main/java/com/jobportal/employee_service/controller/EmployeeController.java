package com.jobportal.employee_service.controller;

import com.jobportal.employee_service.dto.EmployeeRequestDTO;
import com.jobportal.employee_service.entity.Employee;
import com.jobportal.employee_service.service.impl.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeServiceImpl service;

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody EmployeeRequestDTO dto) {
        Employee createdEmployee = service.createEmployee(dto);
        return ResponseEntity.ok(createdEmployee);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        try {
            Employee employee = service.getEmployeeById(id);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequestDTO dto) {
        Employee updatedEmployee = service.updateEmployee(id, dto);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        service.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<java.util.List<Employee>> getAllEmployees() {
        List<Employee> employees = service.getEmployees();
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/{id}/resume")
    public ResponseEntity<String> uploadResume(@PathVariable Long id, @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        String resumeUrl = service.uploadResume(id, file);
        return ResponseEntity.ok(resumeUrl);
    }

    @PostMapping("/{id}/photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable Long id, @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        String photoUrl = service.uploadPhoto(id, file);
        return ResponseEntity.ok(photoUrl);
    }
}
