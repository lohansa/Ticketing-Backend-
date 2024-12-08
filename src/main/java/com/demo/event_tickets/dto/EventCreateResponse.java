package com.demo.event_tickets.dto;

public class EventCreateResponse {
    private String eventId;

    public EventCreateResponse(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}