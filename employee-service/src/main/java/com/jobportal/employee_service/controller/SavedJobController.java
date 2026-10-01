package com.jobportal.employee_service.controller;

import com.jobportal.employee_service.entity.SavedJob;
import com.jobportal.employee_service.service.ISavedJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees/{employeeId}/saved-jobs")
@RequiredArgsConstructor
public class SavedJobController {

    private final ISavedJobService savedJobService;

    @PostMapping
    public ResponseEntity<SavedJob> saveJob(@PathVariable Long employeeId, @RequestBody SavedJob savedJob) {
        return ResponseEntity.ok(savedJobService.saveJob(employeeId, savedJob));
    }

    @GetMapping
    public ResponseEntity<List<SavedJob>> getSavedJobs(@PathVariable Long employeeId) {
        return ResponseEntity.ok(savedJobService.getSavedJobsByEmployeeId(employeeId));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteSavedJob(@PathVariable Long employeeId, @PathVariable Long jobId) {
        savedJobService.deleteSavedJob(employeeId, jobId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{jobId}/check")
    public ResponseEntity<Map<String, Boolean>> isJobSaved(@PathVariable Long employeeId, @PathVariable Long jobId) {
        boolean saved = savedJobService.isJobSaved(employeeId, jobId);
        return ResponseEntity.ok(Map.of("isSaved", saved));
    }
}
