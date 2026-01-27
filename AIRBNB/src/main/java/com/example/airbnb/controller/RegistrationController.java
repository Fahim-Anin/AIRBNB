package com.example.airbnb.controller;

import com.example.airbnb.dto.UserRegistrationDTO;
import com.example.airbnb.model.User;
import com.example.airbnb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth") // Sets the base URL for all methods in this class
public class RegistrationController {

    @Autowired
    private UserService userService;

    // The full URL will be: http://localhost:8080/api/auth/registration
    @PostMapping("/registration")
    public String register(@RequestBody UserRegistrationDTO registrationDto) {
        return userService.registration(registrationDto);
    }
    @GetMapping("/getUsersList")
     public List<User> getUsersList()
    {
        return userService.usersList();
    }

}