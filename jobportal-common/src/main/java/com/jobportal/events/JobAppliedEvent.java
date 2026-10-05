package com.jobportal.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class JobAppliedEvent implements JobPortalEvent {

    private Long jobId;
    private String employerName;
    private String employerEmail;
    private String title;
    private String firstName;
    private String lastName;
    private String email;

}

