package com.example.demo.service;

import com.example.demo.model.ForumPosts;
import com.example.demo.repository.ForumPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class ForumPostService {

    @Autowired
    private ForumPostRepository forumPostRepository;

    public List<ForumPosts> findAll() {
        return forumPostRepository.findAll();
    }

    public ForumPosts save(ForumPosts forumPosts) {
        return forumPostRepository.save(forumPosts);
    }

    public Optional<ForumPosts> findById(Long id) {
        return forumPostRepository.findById(id);
    }
}
