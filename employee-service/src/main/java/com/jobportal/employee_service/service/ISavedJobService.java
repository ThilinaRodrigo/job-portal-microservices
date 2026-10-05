package com.jobportal.employee_service.service;

import com.jobportal.employee_service.entity.SavedJob;

import java.util.List;

public interface ISavedJobService {

    SavedJob saveJob(Long employeeId, SavedJob savedJob);

    List<SavedJob> getSavedJobsByEmployeeId(Long employeeId);

    void deleteSavedJob(Long employeeId, Long jobId);

    boolean isJobSaved(Long employeeId, Long jobId);
}
