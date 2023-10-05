package com.example.models;

import com.google.firebase.firestore.auth.User;

public class UserModel {
    String username;
    String fullname;
    String email;
    String password;
    String usertype;
    String profileImg;
    public UserModel(){

    }

    public UserModel(String username, String fullname, String email, String password, String usertype) {
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.usertype = usertype;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}
