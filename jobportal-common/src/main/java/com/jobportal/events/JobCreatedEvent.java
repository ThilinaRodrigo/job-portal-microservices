package com.jobportal.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class JobCreatedEvent implements JobPortalEvent {
    private Long jobId;
    private String title;
    private Long employerId;
    private String employerEmail;
    private String employerName;
}
