package com.example.demo;

import com.example.demo.model.House;
import com.example.demo.repository.HouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HouseRentalApplication implements CommandLineRunner {

    @Autowired
    private HouseRepository houseRepository;

    public static void main(String[] args) {
        SpringApplication.run(HouseRentalApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
