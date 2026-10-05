package com.jobportal.auth_service.service.impl;

import com.jobportal.auth_service.service.ISmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class TextitSmsServiceImpl implements ISmsService {

    @Value("${textit.biz.url:https://textit.biz/sendmsg}")
    private String textitUrl;

    @Value("${textit.biz.api-key:}")
    private String apiKey;

    @Value("${textit.biz.id:}")
    private String textitId;

    @Value("${textit.biz.pw:}")
    private String textitPw;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean sendSms(String recipientPhone, String messageText) {
        String formattedPhone = normalizePhoneNumber(recipientPhone);
        log.info("Sending SMS via Textit.biz to {} (raw: {})", formattedPhone, recipientPhone);

        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(textitUrl);

            // API Key Authentication or Account ID/Password
            if (apiKey != null && !apiKey.trim().isEmpty()) {
                builder.queryParam("acc", apiKey.trim());
            } else {
                builder.queryParam("id", textitId != null ? textitId.trim() : "")
                       .queryParam("pw", textitPw != null ? textitPw.trim() : "");
            }

            String uri = builder.queryParam("to", formattedPhone)
                    .queryParam("text", messageText)
                    .toUriString();

            log.info("Executing Textit.biz SMS API request for recipient {}", formattedPhone);
            String response = restTemplate.getForObject(uri, String.class);
            log.info("Textit.biz SMS API Response: {}", response);

            return response != null && (response.contains("OK") || response.contains("100") || response.contains("OK:"));
        } catch (Exception e) {
            log.error("Failed to dispatch SMS via Textit.biz gateway: {}", e.getMessage(), e);
            return false;
        }
    }

    private String normalizePhoneNumber(String raw) {
        if (raw == null) return "";
        String cleaned = raw.replaceAll("[^0-9]", "");
        if (cleaned.startsWith("94") && cleaned.length() == 11) {
            return "0" + cleaned.substring(2);
        }
        return cleaned;
    }
}
