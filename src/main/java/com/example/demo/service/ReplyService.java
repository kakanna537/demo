package com.example.demo.service;

import com.example.demo.model.Reply;
import com.example.demo.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    public List<Reply> findByPostId(Long postId) {
        return replyRepository.findByPostId(postId);
    }

    public Reply save(Reply reply) {
        return replyRepository.save(reply);
    }
}
