package com.demo.event_tickets.simulation;

import java.util.Objects;

public record EventTicket(String eventId, int ticketCount) {
    public EventTicket {
        Objects.requireNonNull(eventId);
        if (ticketCount < 0) throw new IllegalArgumentException("Ticket count cannot be negative");
    }

    public EventTicket withDecrementedCount() {
        return new EventTicket(eventId, ticketCount - 1);
    }
}