package com.example.pushnotification.model;

public class User extends AddImage {

    String userid,userName,userEmail,userPassowrd;

    public User() {
    }

    public User(String userName, String userPassowrd) {
        this.userName = userName;
        this.userPassowrd = userPassowrd;
    }

    public User(String userName, String userEmail, String userPassowrd) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassowrd = userPassowrd;
    }

    public User(String userId, String userName, String userEmail, String userPassowrd) {
        this.userid = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassowrd = userPassowrd;
    }

    public String getUserId() {
        return userid;
    }

    public void setUserId(String userId) {
        this.userid = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassowrd() {
        return userPassowrd;
    }

    public void setUserPassowrd(String userPassowrd) {
        this.userPassowrd = userPassowrd;
    }
}
