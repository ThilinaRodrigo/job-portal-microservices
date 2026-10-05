package com.jobportal.auth_service.service;

public interface ISmsService {
    boolean sendSms(String recipientPhone, String messageText);
}
