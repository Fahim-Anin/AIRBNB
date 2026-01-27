package com.example.airbnb.repository;

import com.example.airbnb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // This allows us to check the database for an existing email
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}