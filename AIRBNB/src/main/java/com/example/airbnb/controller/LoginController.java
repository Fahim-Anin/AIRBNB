package com.example.airbnb.controller;

import com.example.airbnb.dto.UserLoginDto;
import com.example.airbnb.service.JwtService;
import com.example.airbnb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import javax.crypto.SecretKey;
import javax.print.PrintService;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private UserService userService;
//    Check the password using String logic
//    @PostMapping("/login")
//    public ResponseEntity<String>login(@RequestBody UserLoginDto loginDto)
//    {
//        try{
//            String result= userService.loginUser(loginDto);
//            if(result.equals("Login Successful!"))
//            {
//                return ResponseEntity.ok(result);
//            }
//            else {
//                return ResponseEntity.status(401).body(result);
//            }
//        }
//        catch(Exception e)
//        {
//            return ResponseEntity.status(404).body(e.getMessage());
//        }
//    }
////Check the password using booloean login
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody UserLoginDto loginDto) {
        // 1. Let the service return a simple true/false
//        boolean isSuccess = userService.loginUser(loginDto);
//
//        if (isSuccess) {
//            // HTTP 200
//            return ResponseEntity.ok("Login Successful");
//
//        } else {
//            // HTTP 401 - Standard for any login failure (wrong email OR wrong pass)
//            return ResponseEntity.status(401).body("Invalid Email or Password");
//        }

//        // 2. FILTER 2: Handle Username/Password Authentication
//        try{
//            String result= userService.loginUser(loginDto);
//            if(result.equals("Invalid Password!"))
//            {
//                return ResponseEntity.status(401).body(result);
//
//            }
//            else {
//                return ResponseEntity.ok(result);
//            }
//        }
//        catch(Exception e)
//        {
//            return ResponseEntity.status(404).body(e.getMessage());
//        }
//    }




        @PostMapping("/login")
        public ResponseEntity<String> login(@RequestBody UserLoginDto loginDto, HttpServletRequest request) {
            // 1. FILTER 1: Check if JwtFilter already validated a token
            String existingUser = (String) request.getAttribute("authenticatedUser");
            if (existingUser != null) {
                return ResponseEntity.ok("Already Logged In as: " + existingUser);
            }

            // 2. FILTER 2: Handle Username/Password Authentication
            try {
                String token = userService.loginUser(loginDto);

                if (token != null) {
                    return ResponseEntity.ok(token); // Return the JWT
                } else {
                    return ResponseEntity.status(401).body("Invalid Password!");
                }
            } catch (Exception e) {
                return ResponseEntity.status(404).body(e.getMessage());
            }
        }


}
