package com.example.airbnb.controller;

import com.example.airbnb.dto.AddPropertyDTO;
import com.example.airbnb.service.PropertyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @PostMapping("/property")
    public ResponseEntity<String> addProperty(@RequestBody AddPropertyDTO addProperty, HttpServletRequest request) {
        String email = (String) request.getAttribute("authenticatedUser");
        String role = (String) request.getAttribute("userRole");

        // 1. Check Authentication (Are they even logged in?)
        if (email == null) {
            return ResponseEntity.status(401).body("User is not logged in");
        }

        // 2. Check Authorization (Are they an OWNER?)
        if ("ROLE_OWNER".equals(role)) {
            propertyService.addpropertyservice(addProperty, email);
            return ResponseEntity.ok("Successfully added property by " + email);
        } else {
            // 3. Forbidden (Logged in, but wrong role)
            return ResponseEntity.status(403).body("Only Owners can add properties! Your role is: " + role);
        }
    }
}