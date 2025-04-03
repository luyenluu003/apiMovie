package com.alibou.security.feature.chatbot.service;

import com.alibou.security.feature.chatbot.model.ChatMessage;
import com.alibou.security.feature.chatbot.model.ChatbotResponse;

import java.util.List;

public interface ChatBotService {

    ChatbotResponse processChatRequest(String userMessage, List<ChatMessage> chatHistory);
}
