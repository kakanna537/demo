package com.example.demo.controller;

import com.example.demo.model.Tenant;
import com.example.demo.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    // POST: 创建或更新租赁者信息
    @PostMapping
    public Tenant createOrUpdateTenant(@RequestBody Tenant tenant) {
        return tenantService.saveTenant(tenant);
    }

    // GET: 根据用户名获取租赁者信息
    @GetMapping("/{username}")
    public Tenant getTenantByUsername(@PathVariable String username) {
        return tenantService.getTenantByUsername(username);
    }
}
