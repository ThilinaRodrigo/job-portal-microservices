package com.jobportal.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class JobApplicationStatusChangedEvent implements JobPortalEvent {
    private Long applicationId;
    private Long jobId;
    private String jobTitle;
    private Long applicantId;
    private String applicantEmail;
    private String oldStatus;
    private String newStatus;
    private String employerName;
}
