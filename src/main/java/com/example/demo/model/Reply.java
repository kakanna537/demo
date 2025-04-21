package com.example.demo.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private ForumPosts post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public ForumPosts getPost() {
        return post;
    }

    public void setPost(ForumPosts post) {
        this.post = post;
    }
}
