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

        String oldStatus = jobApplication.getStatus() != null ? jobApplication.getStatus().name() : "APPLIED";
        String normalizedStatus = status != null ? status.toUpperCase().trim().replace(" ", "_") : "APPLIED";

        JobApplication updatedApp;
        try {
            jobApplication.setStatus(ApplicationStatus.valueOf(normalizedStatus));
            updatedApp = jobApplicationRepository.save(jobApplication);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        try {
            String applicantEmail = updatedApp.getApplicantEmail();
            String jobTitle = updatedApp.getJobTitle();
            String employerName = updatedApp.getCompanyName();

            if (applicantEmail == null || jobTitle == null) {
                try {
                    EmployeeResponseDTO emp = employeeFeignClient.getEmployeeById(updatedApp.getApplicantId()).getBody();
                    if (emp != null && applicantEmail == null) applicantEmail = emp.getEmail();
                } catch (Exception ignored) {}
                try {
                    JobResponseDTO job = jobFeignClient.getJobById(updatedApp.getJobId()).getBody();
                    if (job != null) {
                        if (jobTitle == null) jobTitle = job.getTitle();
                        if (employerName == null && job.getEmployerId() != null) {
                            try {
                                EmployerResponseDTO employer = employerClient.getById(job.getEmployerId()).getBody();
                                if (employer != null) employerName = employer.getEmployerName();
                            } catch (Exception ignored) {}
                        }
                    }
                } catch (Exception ignored) {}
            }

            if (applicantEmail != null && !applicantEmail.isEmpty()) {
                com.jobportal.events.JobApplicationStatusChangedEvent statusEvent =
                        com.jobportal.events.JobApplicationStatusChangedEvent.builder()
                                .applicationId(updatedApp.getId())
                                .jobId(updatedApp.getJobId())
                                .jobTitle(jobTitle != null ? jobTitle : "Position")
                                .applicantId(updatedApp.getApplicantId())
                                .applicantEmail(applicantEmail)
                                .oldStatus(oldStatus)
                                .newStatus(updatedApp.getStatus().name())
                                .employerName(employerName != null ? employerName : "Employer")
                                .build();
                applicationEventProducer.publishEvent("job-status-topic", statusEvent);
                System.out.println("Status change Kafka event published for recipient: " + applicantEmail);
            } else {
                System.out.println("Could not determine applicant email to send status notification event.");
            }
        } catch (Exception e) {
            System.err.println("Error publishing status change Kafka event: " + e.getMessage());
        }

        return updatedApp;
    }

    @Override
    public List<JobApplication> getAllJobApplications() {
        return jobApplicationRepository.findAll();
    }

}
