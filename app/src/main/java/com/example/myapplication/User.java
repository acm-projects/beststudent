package com.example.myapplication;

public class User {
    private String userName;
    private String email;

    public User(String name, String emailAddress) {
        userName = name;
        email = emailAddress;
    }

    public String getUserName() {
        return userName;
    }
    public String getEmail() {
        return email;
    }
}
