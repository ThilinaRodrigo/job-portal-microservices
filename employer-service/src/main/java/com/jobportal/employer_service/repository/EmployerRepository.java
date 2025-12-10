package com.jobportal.employer_service.repository;

import com.jobportal.employer_service.entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerRepository extends JpaRepository<Employer, Long> {

    Employer findByUserId(Long userId);
}
