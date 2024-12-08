package com.demo.event_tickets.config;

import com.demo.event_tickets.service.SimulationService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final SimulationService simulationService;

    public WebSocketConfig(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(simulationService), "/ws")
                .setAllowedOrigins("*");
    }
}