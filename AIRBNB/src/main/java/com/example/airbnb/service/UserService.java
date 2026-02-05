package com.example.airbnb.service;
import com.example.airbnb.dto.UserLoginDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Map;
import java.util.Optional;
// import com.example.airbnb.dto.UserLoginDto;
import com.example.airbnb.dto.UserRegistrationDTO;
import com.example.airbnb.model.Role;
import com.example.airbnb.model.User;
import com.example.airbnb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
//    @Autowired
    //private JwtService jwtService;
//    @Autowired
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
//    @Autowired
//    private PasswordEncoder passwordEncoder;

//    public String registration(User user) {

//        // 1. Validation: Check for null OR empty values
//        // We check (user.getEmail() == null) first to prevent a crash
//        if (user.getEmail() == null || user.getEmail().isEmpty() ||
//                user.getPassword() == null || user.getPassword().isEmpty() ||
//                user.getFullName() == null || user.getFullName().isEmpty() ||
//                user.getRole() == null) {
//
//            return "Fail: Email, Password, or Full Name cannot be empty!";
//        }
//
//        // 1. Check if the email already exists in the 'users' table
//        if (userRepository.existsByEmail(user.getEmail())) {
//            return "Fail: Email is already registered!";
//        }
//
////        set the encrypted password to user
//        user.setPassword(encoder.encode(user.getPassword()));
//
//        // 2. The userRepository.save() method takes the Java Object
//        // and converts it into a SQL INSERT INTO users... statement
//        userRepository.save(user);
//
//        // 3. Return success message
//        return "User registered successfully!";
//    }

//        Using DTO

        public String registration(UserRegistrationDTO dto) {

            if (dto.getEmail() == null || dto.getEmail().isEmpty() ||
                dto.getPassword() == null || dto.getPassword().isEmpty() ||
                dto.getFullName() == null || dto.getFullName().isEmpty() ||
                 dto.getRole() == null)
            {
            return "Fail: Email, Password, or Full Name, role cannot be empty!";
      }
            if(userRepository.existsByEmail(dto.getEmail()))
      {
          return "Fail: Email already exists!";
      }
        // 2. Create the actual Entity object
        User user = new User();
        try{
            user.setEmail(dto.getEmail());
        }
        catch(IllegalArgumentException e) {
            return "Fail: Invalid Email name provided!";
        }
        try {
            user.setFullName(dto.getFullName());
        }

        catch(IllegalArgumentException e) {
            return "Fail: Invalid Full name provided!";
        }
        try {
                user.setRole(Role.valueOf(dto.getRole().toUpperCase().trim()));
            }
        catch (IllegalArgumentException e) {
                return "Fail: Invalid Role name provided!";
            }
//        user.setRole(Role.valueOf(dto.getRole())); // Convert the String from DTO into the Role Enum

        // 3. Hash the password from the DTO
        user.setPassword(encoder.encode(dto.getPassword()));

        // 4. Save to DB
        userRepository.save(user);
        return "User registered successfully!";
    }


    public List<User> usersList() {
        return userRepository.findAll();
    }
    //
    @Autowired
    private JwtService JwtService;
     //Check the password using String logic
    public String loginUser(UserLoginDto loginDto) {

//    JWTFilter check the token and If there is "No Token" then in JWTFILTER:
//    No Attribute is Set: The line request.setAttribute("authenticatedUser", username) is never reached.
//    The Pass-Through: The filter hits filterChain.doFilter(request, response).
//    What happens next? The request simply continues down the "Conveyor Belt" to your Controller.
//    If the user is calling /login: This is perfect! The Controller takes the email/password, validates them, and then calls the JwtService to create a brand new token.

        // 1. Fetch user by email
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // 2. Match raw password with hashed password from DB
        // encoder.matches(rawPassword, hashedPassword)
        if (encoder.matches(loginDto.getPassword(), user.getPassword())) {
//            return "Login Sucessful!";
            // we need to send the role and email to generate the token.
            return JwtService.generateToken(user.getEmail(), user.getRole().toString());
        } else {
            return "Invalid Password!";
        }
    }


//        public boolean loginUser(UserLoginDto loginDto) {
//            User user = userRepository.findByEmail(loginDto.getEmail()).orElse(null);
//
//            if (user == null) {
//                return false;
//            }
//
//            return encoder.matches(loginDto.getPassword(), user.getPassword());
//        }
//


}






