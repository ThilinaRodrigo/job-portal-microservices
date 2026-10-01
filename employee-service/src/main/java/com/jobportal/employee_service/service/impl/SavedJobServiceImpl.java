package com.jobportal.employee_service.service.impl;

import com.jobportal.employee_service.entity.SavedJob;
import com.jobportal.employee_service.repository.SavedJobRepository;
import com.jobportal.employee_service.service.ISavedJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SavedJobServiceImpl implements ISavedJobService {

    private final SavedJobRepository savedJobRepository;

    @Override
    @Transactional
    public SavedJob saveJob(Long employeeId, SavedJob savedJob) {
        savedJob.setEmployeeId(employeeId);
        if (savedJob.getSavedDate() == null) {
            savedJob.setSavedDate(LocalDate.now());
        }

        Optional<SavedJob> existing = savedJobRepository.findByEmployeeIdAndJobId(employeeId, savedJob.getJobId());
        if (existing.isPresent()) {
            return existing.get();
        }

        return savedJobRepository.save(savedJob);
    }

    @Override
    public List<SavedJob> getSavedJobsByEmployeeId(Long employeeId) {
        return savedJobRepository.findByEmployeeIdOrderByIdDesc(employeeId);
    }

    @Override
    @Transactional
    public void deleteSavedJob(Long employeeId, Long jobId) {
        savedJobRepository.deleteByEmployeeIdAndJobId(employeeId, jobId);
    }

    @Override
    public boolean isJobSaved(Long employeeId, Long jobId) {
        return savedJobRepository.existsByEmployeeIdAndJobId(employeeId, jobId);
    }
}
