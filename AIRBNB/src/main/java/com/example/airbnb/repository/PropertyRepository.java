package com.example.airbnb.repository;

import com.example.airbnb.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // We will add findByOwnerId here later!
}