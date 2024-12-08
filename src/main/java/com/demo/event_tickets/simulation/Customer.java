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

    public boolean removeTicket() {
        Optional<Event> ticket = ticketPool.buyTicket(this.getId());
        if (ticket.isPresent()) {
            ticketsBought++;
            return true;
        }

        return false;
    }

    @Override
    public void run() {
        int no_events_count = 0;

        while (ticketsBought < 10) {  // Maximum 10 tickets per customer
            if (!removeTicket()) no_events_count++;

            if (no_events_count >= 3) {
                SimulationLogger logger = SimulationLogger.getInstance();
                logger.log("Customer " + this.getUsername() +
                    " could not find any events to buy tickets (tried 3 times). Now Stopping.");
                break;
            }
            try {
                Thread.sleep(100);  // Sleep for half second between purchases
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        SimulationLogger logger = SimulationLogger.getInstance();
        logger.log("Customer " + this.getUsername() + " bought " + ticketsBought + " tickets. Now Stopping.");
    }
}