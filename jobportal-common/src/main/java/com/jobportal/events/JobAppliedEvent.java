package com.jobportal.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class JobAppliedEvent implements JobPortalEvent {

    private Long jobId;
    private Long employeeId;
}
