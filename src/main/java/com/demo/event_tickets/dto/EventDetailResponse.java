package com.demo.event_tickets.dto;

import java.time.LocalDateTime;

public class EventDetailResponse {
    private String eventId;
    private String name;
    private LocalDateTime date;
    private int totalTickets;
    private String vendorName;  // Adding vendor's username

    public EventDetailResponse(String eventId, String name, LocalDateTime date,
                               int totalTickets, String vendorName) {
        this.eventId = eventId;
        this.name = name;
        this.date = date;
        this.totalTickets = totalTickets;
        this.vendorName = vendorName;
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

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}