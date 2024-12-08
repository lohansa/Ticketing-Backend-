package com.demo.event_tickets.simulation;

import com.demo.event_tickets.dto.TicketPurchaseResponse;
import com.demo.event_tickets.model.Event;
import com.demo.event_tickets.model.Ticket;
import com.demo.event_tickets.model.User;
import com.demo.event_tickets.repository.EventRepository;
import com.demo.event_tickets.repository.TicketRepository;
import com.demo.event_tickets.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class TicketPool {
    private static final Logger logger = LoggerFactory.getLogger(TicketPool.class);
    private final List<EventTicket> availableTickets = new ArrayList<>();

    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final TicketRepository ticketRepository;

    private static TicketPool instance;

    private TicketPool(UserRepository userRepository, EventRepository eventRepository, TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
    }

    public static void initialize(UserRepository userRepository, EventRepository eventRepository, TicketRepository ticketRepository) {
        if (instance != null) return;
        instance = new TicketPool(userRepository, eventRepository, ticketRepository);
    }

    public static TicketPool getInstance() throws IllegalStateException {
        if (instance == null) throw new IllegalStateException("TicketPool not initialized");
        return instance;
    }

    public synchronized void addEvent(Event event) {
        Optional<User> vendorOptional = userRepository.findById(event.getVendorId());
        if (vendorOptional.isEmpty()) {
            return;
        }

        Event savedEvent = eventRepository.save(event);

        // Add to ticket pool
        availableTickets.add(new EventTicket(savedEvent.getId(), event.getTotalTickets()));
        logger.info("Vendor {} created event {} with {} tickets at {}",
                event.getVendorId(),
                event.getName(),
                event.getTotalTickets(),
                event.getDate());
    }

    public synchronized Optional<Event> buyTicket(String customerId) {
        if (availableTickets.isEmpty()) {
            return Optional.empty();
        }

        Optional<User> customerOptional = userRepository.findById(customerId);
        if (customerOptional.isEmpty()) {
            return Optional.empty();
        }

        User customer = customerOptional.get();

        // Get random event ticket
        EventTicket ticket = availableTickets.getFirst();

        // Get event from database
        Optional<Event> event = eventRepository.findById(ticket.eventId());
        if (event.isEmpty()) {
            return Optional.empty();
        }

        Optional<Ticket> existingTicket = ticketRepository.findById(customerId);

        // Check if user already has a ticket for this event
        if (existingTicket.isPresent() &&
                existingTicket.get().getEvents().contains(event.get().getId())) {
            return Optional.empty(); // Already have a ticket
        }

        // Update ticket count
        EventTicket updatedTicket = ticket.withDecrementedCount();
        if (updatedTicket.ticketCount() == 0) {
            availableTickets.removeFirst();
        } else {
            availableTickets.set(0, updatedTicket);
        }

        // Update or create user's tickets
        Ticket ticketList;
        if (existingTicket.isPresent()) {
            ticketList = existingTicket.get();
            List<String> events = new ArrayList<>(ticketList.getEvents());
            events.add(event.get().getId());
            ticketList.setEvents(events);
        } else {
            ticketList = new Ticket();
            ticketList.setUid(customerId);
            ticketList.setEvents(Arrays.asList(event.get().getId()));
        }

        ticketRepository.save(ticketList);

        // Log purchase
        System.out.println("Customer " + customer.getUsername() +
                "purchased ticket for event " + event.get().getName() +
                "(" + updatedTicket.ticketCount() + " tickets remaining)"
        );
        // Log purchase
        logger.info("Customer {} purchased ticket for event {} ({} tickets remaining)",
                customerId,
                event.get().getName(),
                updatedTicket.ticketCount());

        return event;
    }
}