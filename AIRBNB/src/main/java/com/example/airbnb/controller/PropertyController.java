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

        if (email == null) return ResponseEntity.status(401).body("Not logged in");
        if (!"ROLE_OWNER".equals(role))
        {
            return ResponseEntity.status(403).body("Unauthorized Role");
        }

        try {
            propertyService.addpropertyservice(addProperty, email);
            return ResponseEntity.ok("Successfully added property!");
        } catch (RuntimeException e) {
            // This catches the "Duplicate Property" message from the service
            return ResponseEntity.status(409).body(e.getMessage()); // 409 = Conflict
        }
    }
}