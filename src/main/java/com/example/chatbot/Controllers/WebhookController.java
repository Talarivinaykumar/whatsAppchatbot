package com.example.chatbot.Controllers;


import com.example.chatbot.Services.NavigationService;
import com.example.chatbot.Services.WhatsAppService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Autowired
    private WhatsAppService whatsAppService;

    @Autowired
    private NavigationService navigationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${whatsapp.verify-token}")
    private String verifyToken;

    @GetMapping("/")
    public String home() {
        return "WhatsApp Chatbot is running!";
    }
    @PostMapping
    public ResponseEntity<String> receiveMessage(@RequestBody String payload) {
        try {
            JsonNode root = objectMapper.readTree(payload);
            String from = extractPhoneNumber(root);
            String messageText = extractMessageText(root);

            if (from != null && messageText != null) {
                String reply = navigationService.getNavigationResponse(messageText);
                whatsAppService.sendReply(from, reply);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("EVENT_RECEIVED");
    }

    @GetMapping
    public ResponseEntity<String> verifyToken(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.verify_token") String token,
            @RequestParam("hub.challenge") String challenge) {

        if ("subscribe".equals(mode) && verifyToken.equals(token)) {
            return ResponseEntity.ok(challenge);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Verification failed");
        }
    }

    private String extractPhoneNumber(JsonNode root) {
        try {
            return root.path("entry").get(0)
                    .path("changes").get(0)
                    .path("value").path("messages").get(0)
                    .path("from").asText();
        } catch (Exception e) {
            return null;
        }
    }

    private String extractMessageText(JsonNode root) {
        try {
            return root.path("entry").get(0)
                    .path("changes").get(0)
                    .path("value").path("messages").get(0)
                    .path("text").path("body").asText();
        } catch (Exception e) {
            return null;
        }
    }
}

