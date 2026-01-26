package com.example.airbnb.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users") // Maps this class to the 'users' table in Postgres
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String fullName;

    @Enumerated(EnumType.STRING) // Saves as "ROLE_GUEST" instead of 0 or 1
    private Role role;
}