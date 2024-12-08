package com.demo.event_tickets.controller;

import com.demo.event_tickets.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimulationController {

    @Autowired
    private SimulationService simulationService;

    @PostMapping("/simulate")
    public ResponseEntity<?> startSimulation() {
        simulationService.startSimulation();
        return ResponseEntity.ok("Simulation started");
    }

    @DeleteMapping("/simulate")
    public ResponseEntity<?> stopSimulation() {
        simulationService.stopSimulation();
        return ResponseEntity.ok("Simulation stopped");
    }
}