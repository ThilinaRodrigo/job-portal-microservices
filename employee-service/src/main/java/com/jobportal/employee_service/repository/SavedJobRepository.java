package com.jobportal.employee_service.repository;

import com.jobportal.employee_service.entity.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

    List<SavedJob> findByEmployeeIdOrderByIdDesc(Long employeeId);

    Optional<SavedJob> findByEmployeeIdAndJobId(Long employeeId, Long jobId);

    boolean existsByEmployeeIdAndJobId(Long employeeId, Long jobId);

    void deleteByEmployeeIdAndJobId(Long employeeId, Long jobId);
}
