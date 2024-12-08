package com.demo.event_tickets.repository;

import com.demo.event_tickets.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketRepository extends MongoRepository<Ticket, String> {
}