package com.jobportal.job_service.service.impl;

import com.jobportal.events.JobCreatedEvent;
import com.jobportal.job_service.dto.JobRequestDTO;
import com.jobportal.job_service.dto.JobResponseDTO;
import com.jobportal.job_service.entity.Job;
import com.jobportal.job_service.mapper.JobMapper;
import com.jobportal.job_service.repository.JobRepository;
import com.jobportal.job_service.service.IJobService;
import com.jobportal.job_service.service.JobEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements IJobService {

    private final JobRepository jobRepository;
    private  final JobEventProducer jobEventProducer;

    @Override
    public JobResponseDTO createJob(JobRequestDTO request) {
        Job job = JobMapper.reqDtoToEntity(request);
        jobRepository.save(job);
        jobEventProducer.publishJobCreated(
                new JobCreatedEvent(job.getId(), job.getTitle(), job.getEmployerId())
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
    public JobResponseDTO updateJob(Long JobId,JobRequestDTO request) {
        Job job = jobRepository.findById(JobId)
                .orElseThrow(()-> new RuntimeException("Job not found"));
        Job updatedJob = JobMapper.reqDtoToEntity(request);
        updatedJob.setId(job.getId());
        jobRepository.save(updatedJob);
        return JobMapper.entityToResDto(updatedJob);
    }
}
