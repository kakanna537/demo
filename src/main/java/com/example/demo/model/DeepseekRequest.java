package com.example.demo.model;

import lombok.Data;
import lombok.Builder;
import java.util.List;
@Data
@Builder
public class DeepseekRequest {
    private String model;
    private List<Message> messages;
 
    @Data
    @Builder
    public static class Message {
        private String role;
        private String content;
    }
}