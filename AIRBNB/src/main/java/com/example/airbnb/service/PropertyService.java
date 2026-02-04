package com.example.airbnb.service;

import com.example.airbnb.dto.AddPropertyDTO;
import com.example.airbnb.model.Property;
import com.example.airbnb.model.User;
import com.example.airbnb.repository.PropertyRepository;
import com.example.airbnb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    public void addpropertyservice(AddPropertyDTO dto, String email) {
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Refined Logic: Duplicate only if same name AND same owner
        if (propertyRepository.existsByNameAndOwner(dto.getPropertyName(), owner)) {
            // Throwing an exception is cleaner than returning a string
            throw new RuntimeException("Duplicate Property: You already have a listing with this name");
        }

        Property property = new Property();
        property.setOwner(owner);
        property.setName(dto.getPropertyName());
        property.setPrice(dto.getPropertyValue());
        propertyRepository.save(property);
    }

}
