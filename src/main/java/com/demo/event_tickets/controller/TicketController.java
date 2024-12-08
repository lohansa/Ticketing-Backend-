package com.demo.event_tickets.controller;

import com.demo.event_tickets.dto.TicketPurchaseRequest;
import com.demo.event_tickets.dto.TicketPurchaseResponse;
import com.demo.event_tickets.model.Event;
import com.demo.event_tickets.model.Ticket;
import com.demo.event_tickets.repository.EventRepository;
import com.demo.event_tickets.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
public class TicketController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @PostMapping("/api/ticket")
    public ResponseEntity<?> purchaseTicket(@RequestBody TicketPurchaseRequest request,
                                            HttpServletRequest servletRequest) {
        String userId = (String) servletRequest.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(new TicketPurchaseResponse("Not authenticated"));
        }

        Optional<Event> eventOptional = eventRepository.findById(request.getEventId());
        if (eventOptional.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new TicketPurchaseResponse("Event not found"));
        }

        Event event = eventOptional.get();

        if (event.getTotalTickets() <= 0) {
            return ResponseEntity.status(400)
                    .body(new TicketPurchaseResponse("Event is sold out"));
        }

        // Check if user already has a ticket for this event
        Optional<Ticket> existingTicket = ticketRepository.findById(userId);
        if (existingTicket.isPresent() &&
                existingTicket.get().getEvents().contains(request.getEventId())) {
            return ResponseEntity.status(400)
                    .body(new TicketPurchaseResponse("You already have a ticket for this event"));
        }

        // Update ticket count
        event.setTotalTickets(event.getTotalTickets() - 1);
        eventRepository.save(event);

        // Update or create user's tickets
        Ticket ticket;
        if (existingTicket.isPresent()) {
            ticket = existingTicket.get();
            List<String> events = new ArrayList<>(ticket.getEvents());
            events.add(request.getEventId());
            ticket.setEvents(events);
        } else {
            ticket = new Ticket();
            ticket.setUid(userId);
            ticket.setEvents(Arrays.asList(request.getEventId()));
        }

        ticketRepository.save(ticket);

        String message = event.getTotalTickets() == 0
                ? "Ticket purchased successfully. This was the last ticket!"
                : "Ticket purchased successfully";

        return ResponseEntity.ok(new TicketPurchaseResponse(message));
    }
}