package com.example.airbnb.controller;

import com.example.airbnb.dto.BookingDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class BookingController {

    @PostMapping("/booking")
    private ResponseEntity<String> booking(@RequestBody BookingDTO bookingDTO, HttpServletRequest request)
    {
        
    }
}
