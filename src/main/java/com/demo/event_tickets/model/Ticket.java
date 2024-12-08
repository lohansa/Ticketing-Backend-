package com.demo.event_tickets.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "tickets")
public class Ticket {
    @Id
    private String uid;  // Using user's ID as document ID
    private List<String> events;  // List of event IDs

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }
}