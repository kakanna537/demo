package com.example.demo.repository;

import com.example.demo.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    // 根据用户名查找房主信息
    Owner findByUsername(String username);
}
