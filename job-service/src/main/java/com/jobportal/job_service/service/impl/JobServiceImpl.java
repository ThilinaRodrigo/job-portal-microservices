package com.jobportal.job_service.service.impl;

import com.jobportal.events.JobCreatedEvent;
import com.jobportal.job_service.dto.JobRequestDTO;
import com.jobportal.job_service.dto.JobResponseDTO;
import com.jobportal.job_service.dto.client.EmployerResponseDTO;
import com.jobportal.job_service.entity.Job;
import com.jobportal.job_service.mapper.JobMapper;
import com.jobportal.job_service.repository.JobRepository;
import com.jobportal.job_service.service.IJobService;
import com.jobportal.job_service.service.JobEventProducer;
import com.jobportal.job_service.service.client.EmployerFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements IJobService {

    private final JobRepository jobRepository;
    private  final JobEventProducer jobEventProducer;
    private final EmployerFeignClient employerFeignClient;

    @Override
    public JobResponseDTO createJob(JobRequestDTO request) {
        if (request.getPostedDate() == null) {
            request.setPostedDate(java.time.LocalDate.now());
        }

        EmployerResponseDTO employer = null;
        try {
            ResponseEntity<EmployerResponseDTO> response = employerFeignClient.getById(request.getEmployerId());
            if (response != null && response.getBody() != null) {
                employer = response.getBody();
            }
        } catch (Exception ignored) {
            // Fallback gracefully if employer-service returns 404 or throws error
        }

        if (employer == null) {
            employer = new EmployerResponseDTO();
            employer.setEmployerId(request.getEmployerId());
            employer.setEmployerName("Employer " + request.getEmployerId());
            employer.setEmployerEmail("employer" + request.getEmployerId() + "@jobportal.com");
        }

        Job job = JobMapper.reqDtoToEntity(request);
        jobRepository.save(job);

        try {
            jobEventProducer.publishJobCreated(
                    new JobCreatedEvent(job.getId(), job.getTitle(), job.getEmployerId(), employer.getEmployerEmail(), employer.getEmployerName())
            );
        } catch (Exception e) {
            // Kafka publishing is best-effort if broker is offline during local test
        }

        return JobMapper.entityToResDto(job);
    }

    @Override
    public List<JobResponseDTO> getJobsByEmployer(Long employerId) {
        List<Job> jobs = jobRepository.findByEmployerId(employerId);
        return jobs.stream()
                .map(JobMapper::entityToResDto)
                .toList();
    }

    @Override
    public List<JobResponseDTO> getAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream()
                .map(JobMapper::entityToResDto)
                .toList();
    }

    @Override
    public List<JobResponseDTO> searchJobs(String search, String location, String typeStr) {
        final String cleanType = (typeStr != null && !typeStr.trim().isEmpty()) ? typeStr.trim().toUpperCase() : null;
        com.jobportal.job_service.enums.JobType jobType = null;
        if (cleanType != null) {
            try {
                if (cleanType.equals("INTERNSHIP")) {
                    jobType = com.jobportal.job_service.enums.JobType.INTERNSHIP;
                } else {
                    jobType = com.jobportal.job_service.enums.JobType.valueOf(cleanType);
                }
            } catch (Exception ignored) {}
        }

        final String cleanSearch = (search != null && !search.trim().isEmpty()) ? search.trim().toLowerCase() : null;
        final String cleanLocation = (location != null && !location.trim().isEmpty()) ? location.trim().toLowerCase() : null;
        final com.jobportal.job_service.enums.JobType finalJobType = jobType;

        List<Job> allJobs = jobRepository.findAll();

        List<Job> filtered = allJobs.stream().filter(j -> {
            boolean matchesSearch = cleanSearch == null || 
                (j.getTitle() != null && j.getTitle().toLowerCase().contains(cleanSearch)) ||
                (j.getDescription() != null && j.getDescription().toLowerCase().contains(cleanSearch));
            
            boolean matchesLocation = cleanLocation == null ||
                (j.getLocation() != null && j.getLocation().toLowerCase().contains(cleanLocation));

            boolean matchesType = true;
            if (cleanType != null) {
                if (finalJobType != null && j.getType() == finalJobType) {
                    matchesType = true;
                } else if (cleanType.equals("REMOTE") && j.getLocation() != null && j.getLocation().toLowerCase().contains("remote")) {
                    matchesType = true;
                } else {
                    matchesType = false;
                }
            }

            return matchesSearch && matchesLocation && matchesType;
        }).toList();

        return filtered.stream()
                .map(JobMapper::entityToResDto)
                .toList();
    }

    @Override
    public JobResponseDTO updateJob(Long jobId, JobRequestDTO request) {
        Job existingJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

        if (request.getTitle() != null) existingJob.setTitle(request.getTitle());
        if (request.getDescription() != null) existingJob.setDescription(request.getDescription());
        if (request.getLocation() != null) existingJob.setLocation(request.getLocation());
        if (request.getType() != null) existingJob.setType(request.getType());
        if (request.getClosingDate() != null) existingJob.setClosingDate(request.getClosingDate());
        if (request.getEmployerId() != null) existingJob.setEmployerId(request.getEmployerId());

        Job updatedJob = jobRepository.save(existingJob);
        return JobMapper.entityToResDto(updatedJob);
    }

    @Override
    public JobResponseDTO getJobById(Long jobId) {
        return jobRepository.findById(jobId)
                .map(JobMapper::entityToResDto)
                .orElseThrow(()-> new RuntimeException("Job not found"));
    }
}
