package com.jobportal.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class JobAppliedEvent implements JobPortalEvent {

    private Long jobId;
    private String employerName;
    private String title;
    private String firstName;
    private String lastName;
    private String email;

}
