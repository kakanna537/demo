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
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.model.House;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

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
    @Autowired
    private HouseService houseService;

    /**
     * chat
     * @param userId
     * @param question
     * @author senfel
     * @date 2025/3/13 17:36
     * @return Map<String, Object> 包含AI响应的Map
     */
    @Override
    public Map<String, Object> chat(String userId, String question) {
        try {
            if (question.contains("推荐") || question.contains("找房")) {
                // 1. 获取部分房源（如前50条，防止prompt过长）
                List<House> houses = houseService.findAll();
                if (houses.size() > 50) {
                    houses = houses.subList(0, 50);
                }
                // 2. 构造prompt
                StringBuilder prompt = new StringBuilder();
                prompt.append("你是一个智能房屋推荐助手。请根据用户的需求，从以下房源中推荐最符合要求的3-5套，并给出每个推荐房源的推荐理由（50字左右），返回格式如下：\n");
                prompt.append("ID:1, 理由:xxx\nID:2, 理由:xxx\n...\n");
                prompt.append("用户需求：" + question + "\n");
                prompt.append("可选房源列表：\n");
                for (House h : houses) {
                    prompt.append(String.format("ID:%d, 标题:%s, 价格:%d, 户型:%s, 地址:%s, 面积:%d\n",
                        h.getId(), h.getTitle(), h.getPrice(), h.getRooms(), h.getAddress(), h.getArea()));
                }
                prompt.append("请严格按照上述格式返回，每行一个推荐：ID:xx, 理由:xxx。不要输出任何多余内容，也不要输出多余的文字或总结。\n");

                // 3. 调用deepseek
                String aiResponse = callDeepseekAPI(prompt.toString());
                // 4. 解析AI返回的ID和理由（只取前3个）
                Map<Long, String> idReasonMap = new LinkedHashMap<>();
                if (aiResponse != null) {
                    Pattern p = Pattern.compile("ID:(\\d+), ?理由:([^\\n\\r]+)");
                    Matcher m = p.matcher(aiResponse);
                    int count = 0;
                    while (m.find() && count < 3) {
                        Long id = Long.parseLong(m.group(1));
                        String reason = m.group(2).trim();
                        idReasonMap.put(id, reason);
                        count++;
                    }
                }
                // 5. 查找房源详情
                List<Map<String, Object>> houseList = new ArrayList<>();
                for (House house : houses) {
                    if (idReasonMap.containsKey(house.getId())) {
                        Map<String, Object> h = new HashMap<>();
                        h.put("id", house.getId());
                        h.put("title", house.getTitle());
                        h.put("price", house.getPrice());
                        h.put("image", house.getImage());
                        h.put("address", house.getAddress());
                        h.put("url", "/house/" + house.getId());
                        h.put("reason", idReasonMap.get(house.getId()));
                        houseList.add(h);
                    }
                }
                Map<String, Object> result = new HashMap<>();
                result.put("type", "recommendation");
                result.put("houses", houseList);
                return result;
            }
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
                    Map<String, Object> result = new HashMap<>();
                    result.put("type", "text");
                    result.put("response", content);
                    return result;
                }
            }
        } catch (Exception e) {
            log.error("处理 DeepSeek 请求时发生错误", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("type", "text");
            errorResponse.put("response", "处理请求时发生错误，请稍后重试");
            return errorResponse;
        }
    }

    // 新增：调用deepseek的通用方法
    private String callDeepseekAPI(String prompt) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(dsUrl);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Authorization", "Bearer " + dsKey);
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("model", "deepseek-chat");
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> sysMsg = new HashMap<>();
            sysMsg.put("role", "system");
            sysMsg.put("content", "你是一个智能房屋推荐助手");
            messages.add(sysMsg);
            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", prompt);
            messages.add(userMsg);
            requestMap.put("messages", messages);
            requestMap.put("stream", false);
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
                JsonNode responseNode = objectMapper.readTree(jsonResponse.toString());
                String content = responseNode.path("choices")
                        .path(0)
                        .path("message")
                        .path("content")
                        .asText("");
                return content;
            }
        } catch (Exception e) {
            log.error("调用deepseek推荐API失败", e);
            return null;
        }
    }
}