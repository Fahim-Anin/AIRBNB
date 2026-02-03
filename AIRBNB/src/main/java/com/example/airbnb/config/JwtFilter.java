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

// @Component tells Spring: "Create this filter and plug it into the security line automatically."
@Component
public class JwtFilter extends OncePerRequestFilter { // Ensures this logic runs exactly ONCE per API call.

    @Autowired
    private JwtService jwtService; // Injects your helper for token math (decoding/verifying).

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. We grab the "Authorization" string from the HTTP Header.
        // In Postman, this is where you put "Bearer <token>".
        String authHeader = request.getHeader("Authorization");

        String token = null;
        String userEmail = null;

        // 2. We check: Is there a header AND does it start with the word "Bearer "?
        // Industry standard uses "Bearer " to signify this is a JWT token.
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            // 3. We cut off the first 7 characters ("Bearer ") to get only the raw JWT string.
            token = authHeader.substring(7);

            try {
                // 4. We ask our Service to decode the token and find the 'sub' (email).
                // If the signature is fake or the time is expired, this will throw an Exception.
                userEmail = jwtService.extractUsername(token);
                System.out.println(userEmail);
            } catch (Exception e) {
                // If it fails, we catch the error so the app doesn't crash.
                // The request continues but 'username' stays null.
            }
        }

        // 5. If extraction was successful, we now have a valid Identity.
        if (userEmail!= null) {
            // We store the username in the 'Request' object like a temporary sticker.
            // This allows your Controller to see who is logged in later and sends authenticateduser to LoginController
            request.setAttribute("authenticatedUser", userEmail);
        }

        // 6. CRITICAL: This line tells the request to move to the NEXT filter in line.
        // If you forget this, the request will hang forever and never reach your Controller.
        filterChain.doFilter(request, response);
    }
//    If there is "No Token"
//    If the filter checks the header and finds it empty:
//
//    The Code Skips the if: The logic inside if (authHeader != null...) is ignored.
//
//    Username stays null: The variable username remains empty.
//
//    No Attribute is Set: The line request.setAttribute("authenticatedUser", username) is never reached.
//
//    The Pass-Through: The filter hits filterChain.doFilter(request, response).
//
//    What happens next? The request simply continues down the "Conveyor Belt" to your Controller.
//
//    If the user is calling /login: This is perfect! The Controller takes the email/password, validates them, and then calls the JwtService to create a brand new token.
//
}