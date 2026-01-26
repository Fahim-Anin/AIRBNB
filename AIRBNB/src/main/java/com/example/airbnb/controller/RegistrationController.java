package com.example.airbnb.controller;

import com.example.airbnb.model.User;
import com.example.airbnb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Sets the base URL for all methods in this class
public class RegistrationController {

    @Autowired
    private UserService userService;

    // The full URL will be: http://localhost:8080/api/auth/registration
    @PostMapping("/registration")
    public String registration(@RequestBody User user) {
        // Sends the user data from Postman to the Service layer
        return userService.registration(user);
    }
}