package com.demo.event_tickets.simulation;

import com.demo.event_tickets.model.Event;
import com.demo.event_tickets.model.User;
import java.util.Optional;

public class Customer extends User implements SimulationUser {
    private final TicketPool ticketPool;
    private int ticketsBought = 0;

    public Customer(User u) {
        this.setUsername(u.getUsername());
        this.setEmail(u.getEmail());
        this.setId(u.getId());
        this.setVendor(false);

        this.ticketPool = TicketPool.getInstance();
    }

    public void removeTicket() {
        Optional<Event> ticket = ticketPool.buyTicket(this.getId());
        if (ticket.isPresent()) {
            ticketsBought++;
        }
    }

    @Override
    public void run() {
        while (ticketsBought < 10) {  // Maximum 10 tickets per customer
            removeTicket();

            try {
                Thread.sleep(100);  // Sleep for half second between purchases
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}