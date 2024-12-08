package com.demo.event_tickets.simulation;

import com.demo.event_tickets.model.User;
import com.demo.event_tickets.model.Event;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class Vendor extends User implements SimulationUser {
    private static final List<String> EVENT_NAMES = List.of(
            "Rock Concert", "Jazz Night", "Classical Symphony",
            "Pop Festival", "EDM Party", "Country Music Show",
            "Metal Concert", "Blues Night", "Hip Hop Festival",
            "Indie Rock Show"
    );

    private static final Random random = new Random();
    private final TicketPool ticketPool;
    private int eventsCreated = 0;

    public Vendor(User user) {
        this.setUsername(user.getUsername());
        this.setEmail(user.getEmail());
        this.setId(user.getId());
        this.setVendor(true);

        this.ticketPool = TicketPool.getInstance();
    }

    public void addTickets(int count) {
        String randomName = EVENT_NAMES.get(random.nextInt(EVENT_NAMES.size()));
        LocalDateTime futureDate = LocalDateTime.now().plusDays(random.nextInt(30) + 1);

        Event event = new Event();
        event.setName(randomName);
        event.setDate(futureDate);
        event.setTotalTickets(count);
        event.setVendorId(this.getId());

        ticketPool.addEvent(event);
        eventsCreated++;
    }

    @Override
    public void run() {
        int totalTickets = 0;

        while (totalTickets < 1000) {  // 1000 tickets total (10 events * 100 tickets each)
            addTickets(100);
            totalTickets += 100;

            try {
                Thread.sleep(1000);  // Sleep for 1 second between events
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        SimulationLogger logger = SimulationLogger.getInstance();
        logger.log("Vendor " + this.getUsername() + " created " + eventsCreated + " events. Now Stopping.");
    }
}