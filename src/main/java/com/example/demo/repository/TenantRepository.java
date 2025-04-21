package com.example.demo.repository;

import com.example.demo.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    // 根据用户名查找租赁者信息
    Tenant findByUsername(String username);
}
