package com.jobportal.application_service.service.Impl;

import com.jobportal.application_service.dto.JobApplicationRequestDTO;
import com.jobportal.application_service.dto.client.EmployeeResponseDTO;
import com.jobportal.application_service.dto.client.JobResponseDTO;
import com.jobportal.application_service.entity.JobApplication;
import com.jobportal.application_service.enums.ApplicationStatus;
import com.jobportal.application_service.exception.ResourceNotFoundException;
import com.jobportal.application_service.mapper.JobApplicationMapper;
import com.jobportal.application_service.repository.JobApplicationRepository;
import com.jobportal.application_service.service.ApplicationEventProducer;
import com.jobportal.application_service.service.IJobApplicationService;
import com.jobportal.application_service.service.client.EmployeeFeignClient;
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

    @Override
    public JobApplication applyForJob(JobApplicationRequestDTO dto) {

        EmployeeResponseDTO emp = employeeFeignClient.getEmployeeById(dto.getApplicantId()).getBody();
        JobResponseDTO job = jobFeignClient.getJobById(dto.getJobId()).getBody();

        if (emp == null) {
            throw new ResourceNotFoundException("Applicant Not Found");
        }
        if (job == null) {
            throw new ResourceNotFoundException("Job Not Found");
        }

        JobApplication jobApp = JobApplicationMapper.toEntity(dto);
        jobApplicationRepository.save(jobApp);
        applicationEventProducer.publishJobCreated(
                new com.jobportal.events.JobAppliedEvent(jobApp.getJobId(), job.getTitle(), emp.getFirstName(), emp.getLastName(), emp.getEmail())
        );
        jobApp.setId(jobApp.getId());

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

        jobApplication.setStatus(ApplicationStatus.valueOf(status));
        jobApplicationRepository.save(jobApplication);

        return jobApplication;
    }

}
