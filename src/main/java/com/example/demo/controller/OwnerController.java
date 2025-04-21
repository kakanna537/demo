package com.example.demo.controller;

import com.example.demo.model.Owner;
import com.example.demo.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    // POST: 创建或更新房主信息
    @PostMapping
    public Owner createOrUpdateOwner(@RequestBody Owner owner) {
        return ownerService.saveOwner(owner);
    }

    // GET: 根据用户名获取房主信息
    @GetMapping("/{username}")
    public Owner getOwnerByUsername(@PathVariable String username) {
        return ownerService.getOwnerByUsername(username);
    }

    // 检查房主是否已登记
    @GetMapping("/check/{username}")
    public boolean checkOwnerExists(@PathVariable String username) {
        Owner owner = ownerService.getOwnerByUsername(username);
        return owner != null;
    }
}
