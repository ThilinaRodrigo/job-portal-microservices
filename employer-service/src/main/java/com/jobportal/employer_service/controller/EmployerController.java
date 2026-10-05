package com.jobportal.employer_service.controller;

import com.jobportal.employer_service.dto.EmployerRequestDTO;
import com.jobportal.employer_service.entity.Employer;
import com.jobportal.employer_service.service.client.JobFeignClient;
import com.jobportal.employer_service.service.impl.EmployerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employers")
@RequiredArgsConstructor
public class EmployerController {

    private final EmployerServiceImpl employerService;
    private final JobFeignClient jobFeignClient;

    @PostMapping
    public ResponseEntity<Employer> create(@RequestBody EmployerRequestDTO employerRequestDTO) {
        Employer createdEmployer = employerService.createEmployer(employerRequestDTO);
        return ResponseEntity.ok(createdEmployer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employer> getById(@PathVariable Long id) {
        try {
            Employer employer = employerService.getEmployerById(id);
            return ResponseEntity.ok(employer);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employer> update(@PathVariable Long id, @RequestBody EmployerRequestDTO employerRequestDTO) {
        Employer updatedEmployer = employerService.updateEmployer(id, employerRequestDTO);
        return ResponseEntity.ok(updatedEmployer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employerService.deleteEmployer(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/logo")
    public ResponseEntity<String> uploadLogo(@PathVariable Long id, @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        String logoUrl = employerService.uploadLogo(id, file);
        return ResponseEntity.ok(logoUrl);
    }
}
