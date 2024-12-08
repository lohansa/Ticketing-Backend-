package com.demo.event_tickets.service;

import com.demo.event_tickets.repository.EventRepository;
import com.demo.event_tickets.repository.TicketRepository;
import com.demo.event_tickets.repository.UserRepository;
import com.demo.event_tickets.simulation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Service
public class SimulationService {
    private final List<Thread> activeThreads = new ArrayList<>();
    private final SimulationLogger logger = SimulationLogger.getInstance();

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TicketRepository ticketRepository;

    public synchronized void startSimulation() {
        if (!activeThreads.isEmpty()) {
            logger.log("Simulation is already running.");
            return;
        }

        TicketPool.initialize(userRepository, eventRepository, ticketRepository);

        List<SimulationUser> users = userRepository.findAll().stream()
                .map(SimulationUser::create)
                .toList();

        for (SimulationUser user : users) {
            Thread thread = new Thread(user);
            activeThreads.add(thread);
            thread.start();
        }

        Thread cleaner = new Thread(() -> {
            while (!activeThreads.isEmpty()) {
                activeThreads.removeIf(thread -> !thread.isAlive());
                if (activeThreads.isEmpty()) logger.log("Simulation finished.");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        cleaner.start();
    }

    public void stopSimulation() {
        activeThreads.forEach(Thread::interrupt);
    }
}