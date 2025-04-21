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
     * @return Map<String, String> 包含AI响应的Map
     */
    Map<String, String> chat(String userId, String question);
}
