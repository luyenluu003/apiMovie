package com.alibou.security.feature.chatbot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 3341463620012687991L;
    private String role; // "user" hoặc "assistant"
    private String content;
}
