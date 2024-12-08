package com.demo.event_tickets.repository;

import com.demo.event_tickets.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

// Inheritance
public interface UserRepository extends MongoRepository<User, String> {
}