package com.example.chatbot.Services;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WhatsAppService {

    @Value("${whatsapp.access-token}")
    private String accessToken;

    @Value("${whatsapp.phone-number-id}")
    private String phoneNumberId;

    @Value("${whatsapp.api-url}")
    private String apiUrl;

    public void sendReply(String toPhone, String message) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "messaging_product", "whatsapp",
                "to", toPhone,
                "type", "text",
                "text", Map.of("body", message)
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        String url = apiUrl + phoneNumberId + "/messages";

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("Message sent. Response: " + response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

