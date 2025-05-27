package com.example.demo.service;
/**
 * DsChatService
 * @author senfel
 * @version 1.0
 * @date 2025/3/13 17:30
 */
import java.util.Map;

public interface DsChatService {

    /**
     * chat
     * @param userId 用户ID
     * @param question 问题内容
     * @return Map<String, Object> 包含AI响应的Map
     */
    Map<String, Object> chat(String userId, String question);
}
