package com.demo.event_tickets.simulation;

import java.util.List;
import java.util.function.Consumer;

public class SimulationLogger {
    private static SimulationLogger instace;
    private final List<Consumer<String>> subscribers;

    private SimulationLogger() {
        subscribers = new java.util.ArrayList<>();
        subscribers.add(System.out::println);
    }

    public static SimulationLogger getInstance() {
        if (instace == null) {
            instace = new SimulationLogger();
        }

        return instace;
    }

    public void log(String message) {
        subscribers.forEach(subscriber -> subscriber.accept(message));
    }

    public void subscribe(Consumer<String> subscriber) {
        subscribers.add(subscriber);
    }
}
