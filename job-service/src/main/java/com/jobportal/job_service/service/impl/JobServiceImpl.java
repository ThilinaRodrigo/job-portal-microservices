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

        EmployerResponseDTO employer = employerFeignClient.getById(request.getEmployerId()).getBody();
        if (employer == null) {
            throw new RuntimeException("Employer not found");
        }
        Job job = JobMapper.reqDtoToEntity(request);
        jobRepository.save(job);
        jobEventProducer.publishJobCreated(
                new JobCreatedEvent(job.getId(), job.getTitle(), job.getEmployerId(),employer.getEmployerEmail(), employer.getEmployerName())
        );
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
    public JobResponseDTO updateJob(Long JobId,JobRequestDTO request) {
        Job job = jobRepository.findById(JobId)
                .orElseThrow(()-> new RuntimeException("Job not found"));
        Job updatedJob = JobMapper.reqDtoToEntity(request);
        updatedJob.setId(job.getId());
        jobRepository.save(updatedJob);
        return JobMapper.entityToResDto(updatedJob);
    }

    @Override
    public JobResponseDTO getJobById(Long jobId) {
        return jobRepository.findById(jobId)
                .map(JobMapper::entityToResDto)
                .orElseThrow(()-> new RuntimeException("Job not found"));
    }
}
