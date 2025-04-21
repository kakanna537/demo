package com.example.demo.service;

import com.example.demo.model.Owner;
import com.example.demo.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    public Owner saveOwner(Owner owner) {
        return ownerRepository.save(owner);
    }

    public Owner getOwnerByUsername(String username) {
        return ownerRepository.findByUsername(username);
    }
}
