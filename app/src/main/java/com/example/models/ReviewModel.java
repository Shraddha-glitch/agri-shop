package com.example.models;

public class ReviewModel {
    String name;
    String address;
    String review;

    public ReviewModel(){

    }
    public ReviewModel(String name, String address, String review) {
        this.name = name;
        this.address = address;
        this.review = review;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}

