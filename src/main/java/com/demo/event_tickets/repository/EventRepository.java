package com.demo.event_tickets.repository;

import com.demo.event_tickets.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {
}