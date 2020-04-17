package com.example.hackathon.Model;

public class User {
    private String id;
    private String username;
    private String imageURL;
    private String date;
    private String vehicle;
    private int fare;

    //right click and select generate set constructor for all
    public User(String id, String username, String imageURL, String date, int fare, String vehicle) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.date = date;
        this.fare = fare;
        this.vehicle = vehicle;
    }
    //right click and select generate set constructor and select none
    public User() {

    }
    //right click and select generate set set and getter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }
}

