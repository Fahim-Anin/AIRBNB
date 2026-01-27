package com.example.airbnb.controller;

import com.example.airbnb.dto.UserLoginDto;
import com.example.airbnb.model.User;
import com.example.airbnb.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto loginDto, HttpServletResponse response) {
            Map<String, String> tokens = userService.loginUser(loginDto);

            // Set Access Token in Cookie
            ResponseCookie accessCookie = ResponseCookie.from("accessToken", tokens.get("accessToken"))
                    .httpOnly(true).maxAge(300).build();

            // Set Refresh Token in Cookie
            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokens.get("refreshToken"))
                    .httpOnly(true).maxAge(600).build();

            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

            return ResponseEntity.ok("Login Successful! Cookies set.");
        }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {
        try {
            String newAccess = userService.refreshAccessToken(refreshToken);

            ResponseCookie cookie = ResponseCookie.from("accessToken", newAccess)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(300) // 5 mins
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return ResponseEntity.ok("New Access Token Issued");
        } catch (Exception e) {
            // This hits if the 10-minute refresh token is expired (Step 7)
            return ResponseEntity.status(401).body("Session expired. Please login again.");
        }
    }



    }
