package com.jobportal.job_service.service;

import com.jobportal.job_service.dto.JobRequestDTO;
import com.jobportal.job_service.dto.JobResponseDTO;

import java.util.List;

public interface IJobService {

    JobResponseDTO createJob(JobRequestDTO request);
    List<JobResponseDTO> getJobsByEmployer(Long employerId);
    List<JobResponseDTO> getAllJobs();
    JobResponseDTO updateJob(Long jobId,JobRequestDTO request);

}
