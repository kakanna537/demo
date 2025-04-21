package com.example.demo.repository;

import com.example.demo.model.ForumPosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumPostRepository extends JpaRepository<ForumPosts, Long> {
}
