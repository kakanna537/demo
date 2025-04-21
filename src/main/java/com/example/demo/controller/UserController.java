package com.example.demo.controller;

import com.example.demo.model.Users;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users users) {
        if (userService.findByUsername(users.getUsername()) != null) {
            return ResponseEntity.badRequest().body("用户名已存在");
        }
        userService.register(users);
        return ResponseEntity.ok("注册成功");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users users) {
        Users existingUsers = userService.findByUsername(users.getUsername());
        if (existingUsers != null && users.getPassword().equals(existingUsers.getPassword())) {
            // 假设返回一个简单的 token
            return ResponseEntity.ok("登录成功");
        }
        return ResponseEntity.badRequest().body("用户名或密码错误");
    }
}
