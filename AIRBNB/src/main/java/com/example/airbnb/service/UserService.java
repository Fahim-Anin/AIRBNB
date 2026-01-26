package com.example.airbnb.service;

import com.example.airbnb.model.User;
import com.example.airbnb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String registration(User user) {

        // 1. Validation: Check for null OR empty values
        // We check (user.getEmail() == null) first to prevent a crash
        if (user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty() ||
                user.getFullName() == null || user.getFullName().isEmpty() ||
                user.getRole() == null) {

            return "Fail: Email, Password, or Full Name cannot be empty!";
        }

        // 1. Check if the email already exists in the 'users' table
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Fail: Email is already registered!";
        }

        // 2. The userRepository.save() method takes the Java Object
        // and converts it into a SQL INSERT INTO users... statement
        userRepository.save(user);

        // 3. Return success message
        return "User registered successfully!";
    }
}