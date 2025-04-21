package com.example.demo.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DsChatServiceImpl
 * @author senfel
 * @version 1.0
 * @date 2025/3/13 17:31
 */
@Service
@Slf4j
public class DsChatServiceImpl implements DsChatService {
    @Value("sk-8ac1932ca44a41dda8186e93852d095f")
    private String dsKey;
    @Value("https://api.deepseek.com/chat/completions")
    private String dsUrl;
    // 用于保存每个用户的对话历史
    private final Map<String, List<Map<String, String>>> sessionHistory = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * chat
     * @param userId
     * @param question
     * @author senfel
     * @date 2025/3/13 17:36
     * @return Map<String, String> 包含AI响应的Map
     */
    @Override
    public Map<String, String> chat(String userId, String question) {
        try {
            log.info("开始处理问题: {}", question);
            // 获取当前用户的对话历史
            List<Map<String, String>> messages = sessionHistory.getOrDefault(userId, new ArrayList<>());
            // 添加用户的新问题到对话历史
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", question);
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "senfel的AI助手");
            messages.add(userMessage);
            messages.add(systemMessage);

            // 调用 DeepSeek API
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpPost request = new HttpPost(dsUrl);
                request.setHeader("Content-Type", "application/json");
                request.setHeader("Authorization", "Bearer " + dsKey);
                
                Map<String, Object> requestMap = new HashMap<>();
                requestMap.put("model", "deepseek-chat");
                requestMap.put("messages", messages);
                requestMap.put("stream", false); // 设置为非流式
                
                String requestBody = objectMapper.writeValueAsString(requestMap);
                request.setEntity(new StringEntity(requestBody, StandardCharsets.UTF_8));
                
                try (CloseableHttpResponse response = client.execute(request);
                     BufferedReader reader = new BufferedReader(
                             new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8))) {
                    
                    StringBuilder jsonResponse = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonResponse.append(line);
                    }

                    // 解析响应
                    JsonNode responseNode = objectMapper.readTree(jsonResponse.toString());
                    String content = responseNode.path("choices")
                            .path(0)
                            .path("message")
                            .path("content")
                            .asText("");

                    // 将 AI 的回复添加到对话历史
                    Map<String, String> aiMessage = new HashMap<>();
                    aiMessage.put("role", "assistant");
                    aiMessage.put("content", content);
                    messages.add(aiMessage);
                    
                    // 更新会话状态
                    sessionHistory.put(userId, messages);
                    
                    log.info("问题处理完成: {}", question);
                    
                    // 返回响应
                    Map<String, String> result = new HashMap<>();
                    result.put("response", content);
                    return result;
                }
            }
        } catch (Exception e) {
            log.error("处理 DeepSeek 请求时发生错误", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("response", "处理请求时发生错误，请稍后重试");
            return errorResponse;
        }
    }
}