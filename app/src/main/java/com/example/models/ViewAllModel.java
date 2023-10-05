package com.example.models;

import java.io.Serializable;

public class ViewAllModel implements Serializable {

    private boolean isFavorite;
    private String userId;

    String name;
    String description;
    String rating;
    String img_url;
    String type;
    int price;
    private String datetime;
    private String farmerUsername;
    private int stock;
    String documentId;

    public ViewAllModel()  {
    }

    public ViewAllModel(String datetime, String farmerUsername, int stock) {
        this.datetime = datetime;
        this.farmerUsername = farmerUsername;
        this.stock = stock;
    }


    public ViewAllModel(String name, String description, String rating, String img_url, String type, int price,boolean isFavorite,String userId) {
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.img_url = img_url;
        this.type = type;
        this.price = price;
        this.userId = userId;
        this.isFavorite = isFavorite;

    }
    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getFarmerUsername() {
        return farmerUsername;
    }

    public void setFarmerUsername(String farmerUsername) {
        this.farmerUsername = farmerUsername;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
