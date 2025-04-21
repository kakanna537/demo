package com.example.demo.controller;

import com.example.demo.model.ForumPosts;
import com.example.demo.repository.ForumPostRepository;
import com.example.demo.service.ForumPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/forum")
public class ForumPostController {

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private ForumPostService forumPostService;

    @PostMapping("/posts")
    public ForumPosts createPost(@RequestBody ForumPosts post) {
        post.setDate(LocalDateTime.now());
        return forumPostRepository.save(post);
    }

    @GetMapping("/posts")
    public List<ForumPosts> getAllPosts() {
        return forumPostRepository.findAll();
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<ForumPosts> getPostById(@PathVariable Long id) {
        Optional<ForumPosts> post = forumPostService.findById(id);
        return post.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
