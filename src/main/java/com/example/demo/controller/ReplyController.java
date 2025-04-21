package com.example.demo.controller;

import com.example.demo.model.Reply;
import com.example.demo.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/replies")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @GetMapping("/post/{postId}")
    public List<Reply> getRepliesByPostId(@PathVariable Long postId) {
        return replyService.findByPostId(postId);
    }

    @PostMapping
    public Reply createReply(@RequestBody Reply reply) {
        reply.setDate(LocalDateTime.now());
        return replyService.save(reply);
    }
}
