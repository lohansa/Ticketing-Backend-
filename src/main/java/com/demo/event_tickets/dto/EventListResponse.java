package com.demo.event_tickets.dto;

import java.time.LocalDateTime;

public class EventListResponse {
    private String eventId;
    private String name;
    private LocalDateTime date;

    public EventListResponse(String eventId, String name, LocalDateTime date) {
        this.eventId = eventId;
        this.name = name;
        this.date = date;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}