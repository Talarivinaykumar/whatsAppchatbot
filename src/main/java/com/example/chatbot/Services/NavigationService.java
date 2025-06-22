package com.example.chatbot.Services;


import org.springframework.stereotype.Service;

@Service
public class NavigationService {

    public String getNavigationResponse(String userMessage) {
        switch (userMessage.toLowerCase()) {
            case "start":
                return "Welcome to NavBot! Send your current location to begin.";
            case "help":
                return "You can send 'start', 'directions', or a location.";
            default:
                return "I'm not sure what you mean. Try 'help'.";
        }
    }
}

