package com.demo.event_tickets.service;

import com.demo.event_tickets.model.User;
import com.demo.event_tickets.repository.EventRepository;
import com.demo.event_tickets.repository.TicketRepository;
import com.demo.event_tickets.repository.UserRepository;
import com.demo.event_tickets.simulation.Customer;
import com.demo.event_tickets.simulation.SimulationUser;
import com.demo.event_tickets.simulation.TicketPool;
import com.demo.event_tickets.simulation.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class SimulationService {
    private ExecutorService executorService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TicketRepository ticketRepository;

    public SimulationService() {
            this.executorService = Executors.newCachedThreadPool();
    }

    public synchronized void startSimulation() {
        if (executorService == null || executorService.isTerminated()) {
            executorService = Executors.newCachedThreadPool();
        }

        TicketPool.initialize(userRepository, eventRepository, ticketRepository);

        List<SimulationUser> users = userRepository.findAll().stream()
                .map(SimulationUser::create)
                .toList();

        users.forEach(executorService::submit);
    }

    public void stopSimulation() {
        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }

            System.out.println("Simulation stopped");
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
            System.out.println("Simulation stop interrupted");
        }
    }
}