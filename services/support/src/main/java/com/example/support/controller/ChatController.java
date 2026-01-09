package com.example.support.controller;

import com.example.support.model.ChatRequest;
import com.example.support.model.ChatResponse;
import com.example.support.service.ChatbotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/support")
public class ChatController {

    private final ChatbotService chatbotService;

    public ChatController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        if (request == null || !StringUtils.hasText(request.getMessage())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ChatResponse("Message must not be empty.", "validation-error"));
        }
        return ResponseEntity.ok(chatbotService.respond(request.getMessage()));
    }
}
