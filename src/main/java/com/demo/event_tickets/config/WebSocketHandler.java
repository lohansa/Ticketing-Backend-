package com.demo.event_tickets.config;

import com.demo.event_tickets.service.SimulationService;
import com.demo.event_tickets.simulation.SimulationLogger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandler extends TextWebSocketHandler {
    private final Set<WebSocketSession> sessions;
    private final SimulationService simulationService;

    public WebSocketHandler(SimulationService simulationService) {
        this.simulationService = simulationService;
        SimulationLogger.getInstance().subscribe(this::broadcastMessage);

        sessions = ConcurrentHashMap.newKeySet();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        try {
            super.afterConnectionClosed(session, status);
        } catch (Exception e) {
            // Exception is not an issue since session is removed finally
            System.out.println("Exception during afterConnectionClosed.");
        }

        sessions.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        if (payload.equals("START")) {
            simulationService.startSimulation();
        } else if (payload.equals("STOP")) {
            simulationService.stopSimulation();
        }
    }

    private void broadcastMessage(String message) {
        TextMessage textMessage = new TextMessage(message);
        sessions.forEach(session -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(textMessage);
                }
            } catch (Exception e) {
                System.out.println("Failed to send message to client: " + e.getMessage());
            }
        });
    }
}