package com.demo.event_tickets.simulation;

import com.demo.event_tickets.model.User;

public interface SimulationUser extends Runnable {
    static SimulationUser create(User u) {
        if (u.isVendor()) {
            return new Vendor(u);
        } else {
            return new Customer(u);
        }
    }
}
