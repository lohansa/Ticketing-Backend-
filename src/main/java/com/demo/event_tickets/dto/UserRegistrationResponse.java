package com.demo.event_tickets.dto;

public class UserRegistrationResponse {
    private String uid;

    public UserRegistrationResponse(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}