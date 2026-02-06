package com.example.airbnb.controller;

import com.example.airbnb.dto.BookingDTO;
import com.example.airbnb.service.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.example.airbnb.model.Role.ROLE_USER;

@RestController
public class BookingController {

    @Autowired
    private BookingService bookingService;


    @PostMapping("/booking")
    private ResponseEntity<String> booking(@RequestBody BookingDTO bookingDTO, HttpServletRequest request)
    {

        String email =  request.getAttribute("authenticatedUser").toString();
        String role = (String) request.getAttribute("userRole");

        if(email == null)
        {
            return ResponseEntity.status(401).body("Not Logged In");
        }

        if(!"ROLE_USER".equals(role))
        {
            return ResponseEntity.status(403).body("Unauthorized Role");
        }
        try{
            bookingService.addBookingService(bookingDTO , email);
            return ResponseEntity.status(200).body("Booking Successfully Added");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(401).body(e.getMessage());
        }


    }
}
