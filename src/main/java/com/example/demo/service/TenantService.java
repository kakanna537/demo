package com.example.demo.service;

import com.example.demo.model.Tenant;
import com.example.demo.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    public Tenant saveTenant(Tenant tenant) {
        return tenantRepository.save(tenant);
    }

    public Tenant getTenantByUsername(String username) {
        return tenantRepository.findByUsername(username);
    }
}
