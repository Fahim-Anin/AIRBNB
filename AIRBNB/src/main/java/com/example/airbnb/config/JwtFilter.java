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
        // The filter pulls Bearer eyJhbG... from the request.
        String authHeader = request.getHeader("Authorization");

        String token = null;
        String userEmail = null;

        // 2. We check: Is there a header AND does it start with the word "Bearer "?
        // Industry standard uses "Bearer " to signify this is a JWT token.
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            // 3. We cut off the first 7 characters ("Bearer ") to get only the raw JWT string.
            // It strips "Bearer " to get the raw JWT.
            token = authHeader.substring(7);

            try {
                // 4. INDUSTRY PRO-TIP: Extract EVERYTHING inside the try block.
                // If the signature is fake or expired, extractUsername will throw an Exception.
                // JwtService decodes the token, verifies the signature from the extractUserEmail method, and returns "owner@gmail.com".
                userEmail = jwtService.extractUserEmail(token);

                // If we reach this line, the token is 100% valid.
                // Now we grab the Role and set our "Request Stickers".
                // JwtService looks inside the claims for the "role" key and returns "ROLE_OWNER"
                String role = jwtService.extractRole(token);

                // These stickers allow your Controller to see who is logged in and what they can do.
                // The folder first arrives at the Security Desk (JwtFilter).
                // The guard opens the folder, finds a "Keycard" (the Token), and verifies it.
                // The guard doesn't want to re-verify the token every time, so they take a Post-it Note (Attribute), write the email 2@gmail.com on it, and stick it to the front of the folder.
                // The guard gives the label a name: "authenticatedUser".
               //  The folder then moves to the Manager's Office (PropertyController).
               //  The Manager doesn't know how to verify tokens. They just look at the folder, see the Post-it Note named "authenticatedUser", and say: "Ah, I see this folder belongs to 2@gmail.com!"
                request.setAttribute("authenticatedUser", userEmail);
                request.setAttribute("userRole", role);

                System.out.println("Filter logged in user: " + userEmail + " with role: " + role);

            } catch (Exception e) {
                // If it fails (fake token), we catch the error so the app doesn't crash.
                // The request continues but no attributes (stickers) are set.
                System.out.println("Invalid Token detected: " + e.getMessage());
            }
        }

        // 5. CRITICAL: This line tells the request to move to the NEXT filter in line.
        // Even if there is NO token, we must call this so the request reaches the Controller.
        // The guard opens the gate and lets the request move forward to the Controller.
        filterChain.doFilter(request, response);
    }

    /* RECAP OF THE FLOW FOR YOUR NOTES:

      If there is "No Token" (e.g., calling /login):
      - The 'if (authHeader != null...)' is skipped.
      - userEmail stays null.
      - No Attributes are set.
      - filterChain.doFilter() sends the request forward.
      - Result: The LoginController receives the request and generates a brand new token.

      If there is a "Valid Token" (e.g., calling /property):
      - The filter extracts Email and Role.
      - Sets attributes "authenticatedUser" and "userRole".
      - Result: The PropertyController reads the role and allows or blocks the user.
    */
}