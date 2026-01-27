package com.example.airbnb.service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Map;
import java.util.Optional;
import com.example.airbnb.dto.UserLoginDto;
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
    @Autowired
    private JwtService jwtService;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Autowired
    private PasswordEncoder passwordEncoder;

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

        // 1. Validation (use the DTO fields)
        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            return "Fail: Email is required!";
        }

        // 2. Create the actual Entity object
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setRole(Role.valueOf(dto.getRole())); // Convert the String from DTO into the Role Enum

        // 3. Hash the password from the DTO
        user.setPassword(encoder.encode(dto.getPassword()));

        // 4. Save to DB
        userRepository.save(user);
        return "User registered successfully!";
    }


    public List<User> usersList() {
        return userRepository.findAll();
    }


    public Map<String, String> loginUser(UserLoginDto loginDto) {
        // 1. Find User
        User user = userRepository.findByEmail(loginDto.getEmailLogin())
                .orElseThrow(() -> new RuntimeException("Invalid Email !!!"));

        // 2. Compare Bcrypt Passwords (Salt is handled automatically here)


        if (!passwordEncoder.matches(loginDto.getPasswordLogin(), user.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        // 3. Create Tokens (5 min access, 10 min refresh)
        String accessToken = jwtService.generateToken(user.getEmail(), 5 * 60 * 1000);
        String refreshToken = jwtService.generateToken(user.getEmail(), 10 * 60 * 1000);

        return Map.of("access", accessToken, "refresh", refreshToken);
    }
    public String refreshAccessToken(String refreshToken) {
        // extractEmail will throw an exception if the 10 mins (refresh token) have passed
        String email = jwtService.extractEmail(refreshToken);

        // If we get here, the refresh token is valid. Generate a new 5-min access token.
        return jwtService.generateToken(email, 5 * 60 * 1000);
    }

}
