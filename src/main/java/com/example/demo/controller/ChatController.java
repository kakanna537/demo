package com.example.demo.controller;
/**
 * DsController
 * @author senfel
 * @version 1.0
 * @date 2025/3/13 17:21
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import lombok.extern.slf4j.Slf4j;
import com.example.demo.service.DsChatService;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class ChatController {
    @Autowired
    private DsChatService dsChatService;

    /**
     * chat page
     * @param modelAndView
     * @author senfel
     * @date 2025/3/13 17:39
     * @return org.springframework.web.servlet.ModelAndView
     */
    @GetMapping()
    public ModelAndView chat(ModelAndView modelAndView) {
        modelAndView.setViewName("chat");
        return modelAndView;
    }

    /**
     * chat
     * @param requestBody 包含问题的请求体
     * @return Map<String, Object> 包含AI响应的Map
     */
    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestBody Map<String, String> requestBody) {
        String question = requestBody.get("question");
        String userId = "default-user";
        return dsChatService.chat(userId, question);
    }
}
