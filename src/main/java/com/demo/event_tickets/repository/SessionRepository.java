package com.demo.event_tickets.repository;

import com.demo.event_tickets.model.Session;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionRepository extends MongoRepository<Session, String> {
}