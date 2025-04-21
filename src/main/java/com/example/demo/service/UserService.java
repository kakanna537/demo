package com.example.demo.service;

import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public Users register(Users users) {
        Users savedUsers = userRepository.save(users);
        logger.info("User registered: " + savedUsers.getUsername());
        return savedUsers;
    }

    public Users findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
