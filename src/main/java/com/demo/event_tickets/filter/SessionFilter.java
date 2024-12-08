package com.demo.event_tickets.filter;

import com.demo.event_tickets.model.Session;
import com.demo.event_tickets.repository.SessionRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@Order(2)
public class SessionFilter extends OncePerRequestFilter {

    private final SessionRepository sessionRepository;

    public SessionFilter(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSIONID".equals(cookie.getName())) {
                    String sessionId = cookie.getValue();

                    // Use SessionRepository to find the session
                    Optional<Session> sessionOptional = sessionRepository.findById(sessionId);
                    sessionOptional.ifPresent(session -> {
                        if (session.getUserId() != null) {
                            request.setAttribute("userId", session.getUserId());
                        }
                    });
                    break;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}