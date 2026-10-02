package com.jobportal.application_service.service.Impl;

import com.jobportal.application_service.dto.JobApplicationRequestDTO;
import com.jobportal.application_service.dto.client.EmployeeResponseDTO;
import com.jobportal.application_service.dto.client.EmployerResponseDTO;
import com.jobportal.application_service.dto.client.JobResponseDTO;
import com.jobportal.application_service.entity.JobApplication;
import com.jobportal.application_service.enums.ApplicationStatus;
import com.jobportal.application_service.exception.ResourceNotFoundException;
import com.jobportal.application_service.mapper.JobApplicationMapper;
import com.jobportal.application_service.repository.JobApplicationRepository;
import com.jobportal.application_service.service.ApplicationEventProducer;
import com.jobportal.application_service.service.IJobApplicationService;
import com.jobportal.application_service.service.client.EmployeeFeignClient;
import com.jobportal.application_service.service.client.EmployerFeignClient;
import com.jobportal.application_service.service.client.JobFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements IJobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final ApplicationEventProducer applicationEventProducer;
    private final EmployeeFeignClient employeeFeignClient;
    private final JobFeignClient jobFeignClient;
    private final EmployerFeignClient employerClient;

    @Override
    public JobApplication applyForJob(JobApplicationRequestDTO dto) {

        JobApplication jobApp = JobApplicationMapper.toEntity(dto);

        try {
            EmployeeResponseDTO emp = employeeFeignClient.getEmployeeById(dto.getApplicantId()).getBody();
            JobResponseDTO job = jobFeignClient.getJobById(dto.getJobId()).getBody();
            if (job != null) {
                EmployerResponseDTO employer = employerClient.getById(job.getEmployerId()).getBody();
                if (employer != null && emp != null) {
                    applicationEventProducer.publishJobCreated(
                        new com.jobportal.events.JobAppliedEvent(
                            jobApp.getJobId(), 
                            employer.getEmployerName(), 
                            employer.getEmployerEmail(), 
                            job.getTitle(), 
                            emp.getFirstName(), 
                            emp.getLastName(), 
                            emp.getEmail()
                        )
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Non-critical Feign/Event notification error: " + e.getMessage());
        }

        jobApplicationRepository.save(jobApp);
        return jobApp;
    }


    @Override
    public List<JobApplication> getJobApplicationsByJobId(Long jobId) {
        return jobApplicationRepository.getJobApplicationsByJobId(jobId);
    }

    @Override
    public List<JobApplication> getJobApplicationsByApplicantId(Long applicantId) {
        return jobApplicationRepository.getJobApplicationsByApplicantId(applicantId);
    }

    @Override
    public JobApplication getApplicationById(Long applicationId) {
        return jobApplicationRepository.findById(applicationId)
                .orElseThrow(()-> new ResourceNotFoundException("Application Not Found"));
    }

    @Override
    public JobApplication updateApplicationStatus(Long applicationId, String status) {

        JobApplication jobApplication = jobApplicationRepository.findById(applicationId)
                .orElseThrow(()-> new ResourceNotFoundException("Application Not Found"));

        String normalizedStatus = status != null ? status.toUpperCase().trim().replace(" ", "_") : "APPLIED";
        if ("PENDING".equals(normalizedStatus)) {
            normalizedStatus = "APPLIED";
        }

        try {
            jobApplication.setStatus(ApplicationStatus.valueOf(normalizedStatus));
            return jobApplicationRepository.save(jobApplication);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            jobApplication.setStatus(ApplicationStatus.APPLIED);
            return jobApplicationRepository.save(jobApplication);
        }
    }

    @Override
    public List<JobApplication> getAllJobApplications() {
        return jobApplicationRepository.findAll();
    }

}
