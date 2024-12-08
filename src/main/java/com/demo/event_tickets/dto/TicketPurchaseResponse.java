package com.demo.event_tickets.dto;

public class TicketPurchaseResponse {
    private String message;

    public TicketPurchaseResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}