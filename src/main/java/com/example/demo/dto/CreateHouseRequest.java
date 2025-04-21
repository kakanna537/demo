package com.example.demo.dto;

public class CreateHouseRequest {
    private String title;
    private String description;
    private String type;
    private String rooms;
    private String floor;
    private String orientation;
    private String year;
    private int area;
    private int price;
    private String image;
    private String address;
    private double latitude;
    private double longitude;
    private String username;

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getRooms() { return rooms; }
    public void setRooms(String rooms) { this.rooms = rooms; }
    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }
    public String getOrientation() { return orientation; }
    public void setOrientation(String orientation) { this.orientation = orientation; }
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
    public int getArea() { return area; }
    public void setArea(int area) { this.area = area; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
} 