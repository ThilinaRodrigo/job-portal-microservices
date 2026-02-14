package com.jobportal.application_service.service.client;

import com.jobportal.application_service.dto.client.EmployeeResponseDTO;
import com.jobportal.application_service.fallback.EmployeeFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employee-service",
                fallback = EmployeeFeignFallback.class)
public interface EmployeeFeignClient {

    @GetMapping({"employees/{id}"})
    ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id);

}
