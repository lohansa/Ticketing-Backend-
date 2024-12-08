package com.demo.event_tickets.controller;

import com.demo.event_tickets.dto.EventCreateRequest;
import com.demo.event_tickets.dto.EventCreateResponse;
import com.demo.event_tickets.model.Event;
import com.demo.event_tickets.model.User;
import com.demo.event_tickets.repository.EventRepository;
import com.demo.event_tickets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/api/event")
    public ResponseEntity<?> createEvent(@RequestBody EventCreateRequest request, HttpServletRequest servletRequest) {
        String vendorId = (String) servletRequest.getAttribute("userId");
        if (vendorId == null) {
            return ResponseEntity.status(401).body("{\"error\":\"Not authenticated\"}");
        }

        Optional<User> vendorOptional = userRepository.findById(vendorId);
        if (vendorOptional.isEmpty() || !vendorOptional.get().isVendor()) {
            return ResponseEntity.status(403).body("{\"error\":\"Not authorized as vendor\"}");
        }

        Event event = new Event();
        event.setName(request.getName());
        event.setDate(request.getDate());
        event.setTotalTickets(request.getTotalTickets());
        event.setVendorId(vendorId);

        Event savedEvent = eventRepository.save(event);

        return ResponseEntity.ok(new EventCreateResponse(savedEvent.getId()));
    }
}