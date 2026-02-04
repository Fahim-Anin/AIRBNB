package com.example.airbnb.config;

import com.example.airbnb.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.security.SignatureException;

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

            } catch (ExpiredJwtException e) {
//                Every JWT has a payload field called exp. When jwtService.extractUserEmail(token) is called, the JJWT library compares the current system time to the exp time in the token.
//
//                The Logic: If System.currentTimeMillis() is greater than the exp value, the library immediately throws this specific exception.
//
//                The Industry Result: You catch it, send a 401 Unauthorized status, and tell the user, "Your session has expired." This forces them to log in again to get a fresh token.
                // Handle Expired Token
                // First Time The "blank envelope" as Response going back to the user.
                handleFilterError(response, "Your session has expired. Please login again.", HttpServletResponse.SC_UNAUTHORIZED);
                // SC stands for Status Code. SC_UNAUTHORIZED is just a fancy name for the number 40
                // writes the "Session Expired" message into the response object.
                return; // STOP: Do not call filterChain.doFilter() //the filter stops. It never calls the PropertyController
                // travels back through the internet to Postman.


            } catch (Exception e) {
                // Handle any other unexpected security errors
                handleFilterError(response, "Authentication error: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return; // STOP
            }

        }

        // 5. CRITICAL: This line tells the request to move to the NEXT filter in line.
        // Even if there is NO token, we must call this so the request reaches the Controller.
        // The guard opens the gate and lets the request move forward to the Controller.
        filterChain.doFilter(request, response);


    }
    // HttpServletResponse response: This is the "Transmission Pipe" that leads directly back to the user's Postman or Browser.
    // String message: The actual text you want the user to read (e.g., "Your session has expired").
    // int status: The HTTP status code number (like 401, 403, or 500).

//    Line 1: response.setStatus(status);
//    Action: Stamps the "Header" of the response.
//            Effect: In Postman, this is what makes the little status box turn Red and say 401 Unauthorized. It doesn't put text on the screen; it sets the "mood" of the response.

//    Line 2: response.setContentType("application/json");
//    Action: Tells the receiver what type of data is coming.
//            Effect: Without this, Postman might think you are sending a plain text file or a website. By setting it to application/json, Postman knows to use "Pretty Print" so the error looks professional.

//    Line 3: response.getWriter().write("{\"error\": \"" + message + "\"}");
//    Action: The "Ink on Paper" moment.
//            response.getWriter(): Opens the stream (the pen).
//            .write(...): Writes the actual JSON string.
//    The Result: It constructs a valid JSON object like {"error": "Your session has expired"} and sends it down the pipe.

//    throws IOException:
//    The getWriter() method interacts with the network. If the user suddenly closes their laptop or loses internet while your server is trying to write the message,
//    the "pipe" breaks. Java calls this an Input/Output Exception. Since we can't "fix" a broken internet connection, we simply say throws IOException to tell Java, "If the pipe breaks, let the system handle it."
    private void handleFilterError(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status); // This sets the "Header" of the response to 401. Postman sees this and turns the status indicator Red.

        response.setContentType("application/json");
        // This tells Postman, "Hey, I'm not sending you a webpage (HTML), I'm sending you a JSON object." This allows Postman to format the text nicely.
        // Using a JSON format so front-end apps can parse the error easily
        response.getWriter().write("{\"error\": \"" + message + "\"}");
        // This opens the "pipe" to the user's screen and pushes the text {"error": "Your session has expired..."} through it
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