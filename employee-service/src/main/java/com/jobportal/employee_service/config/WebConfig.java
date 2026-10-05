package com.jobportal.employee_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Static file serving is handled by FileServeController
    // which properly URL-decodes Unicode/Sinhala filenames.
}
