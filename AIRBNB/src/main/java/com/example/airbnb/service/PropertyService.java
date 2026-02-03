package com.example.airbnb.service;

import com.example.airbnb.dto.AddPropertyDTO;
import com.example.airbnb.model.Property;
import com.example.airbnb.model.User;
import com.example.airbnb.repository.PropertyRepository;
import com.example.airbnb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    public void addpropertyservice(AddPropertyDTO propertydto, String userEmail) {
        // 1. Find the User object from the DB using the email from the token
        // If the user doesn't exist (very rare since token was valid), we throw an error
        User owner = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        System.out.println(owner);
        Property property = new Property();
        // 3. Set the OWNER object (Hibernate will automatically extract the ID for the foreign key)
        property.setOwner(owner);
        property.setName(propertydto.getPropertyName());
        property.setPrice(propertydto.getPropertyValue());
        propertyRepository.save(property);

    }

}
