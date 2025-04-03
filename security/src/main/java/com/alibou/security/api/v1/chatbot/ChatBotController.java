package com.alibou.security.api.v1.chatbot;

import com.alibou.security.feature.chatbot.model.ChatRequest;
import com.alibou.security.feature.chatbot.model.ChatbotResponse;
import com.alibou.security.feature.chatbot.service.ChatBotService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/v1/chatbot")
public class ChatBotController {

    @Autowired
    private ChatBotService chatBotService;

    @PostMapping("/chat")
    public ResponseEntity<ChatbotResponse> processChat(@RequestBody ChatRequest request) {
        ChatbotResponse response = chatBotService.processChatRequest(
                request.getMessage(),
                request.getChatHistory()
        );
        return ResponseEntity.ok(response);
    }
}
