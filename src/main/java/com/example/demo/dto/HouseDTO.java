package com.example.demo.dto;

import com.example.demo.model.House;
import com.example.demo.model.Owner;

public class HouseDTO {

    private House house;
    private Owner owner;

    public HouseDTO(House house, Owner owner) {
        this.house = house;
        this.owner = owner;
    }


    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
