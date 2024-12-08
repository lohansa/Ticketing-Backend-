package com.demo.event_tickets.dto;

public class UserRegistrationRequest {
    private String username;
    private String email;
    private String password;
    private boolean isVendor;

    // Getters
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isVendor() {
        return isVendor;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsVendor(boolean vendor) {
        isVendor = vendor;
    }
}