package com.example.airbnb.config;

import com.example.airbnb.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // 1. Check if Token exists in Header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                // If this doesn't throw an error, the token is valid!
                username = jwtService.extractUsername(token);
            } catch (Exception e) {
                // Token is fake or expired - we do nothing and let the next filter handle it
            }
        }

        if (username != null) {
            // Logic: Token exists! We "Auto-Login"
            request.setAttribute("authenticatedUser", username);
        }

        filterChain.doFilter(request, response);
    }
}