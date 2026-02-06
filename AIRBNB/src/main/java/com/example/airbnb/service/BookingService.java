package com.example.airbnb.service;

import com.example.airbnb.dto.BookingDTO;
import com.example.airbnb.model.Book;
import com.example.airbnb.model.Property;
import com.example.airbnb.model.User;
import com.example.airbnb.repository.BookingRepository;
import com.example.airbnb.repository.PropertyRepository;
import com.example.airbnb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public void addBookingService(BookingDTO bookingDTO, String email) {

        User userEmail = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(bookingRepository.existsBookByUserEmailAndCheckInDateCheckOutDate(userEmail
                , bookingDTO.getCheckInDate()
                , bookingDTO.getCheckOutDate()))
        {
            throw new RuntimeException("Booking already exists");
        }

        else if(bookingRepository.existsByCheckInDate(bookingDTO.getCheckInDate()))
        {
            throw new RuntimeException("Booking already exists");
        }

        Book book = new Book();
        Property property = new Property();
        property = propertyRepository.findById(bookingDTO.getPropertyId()).orElseThrow(() -> new RuntimeException("Property not found"));
        book.setProperty(property);
        book.setUserEmail(userEmail);

        book.setCheckInDate(bookingDTO.getCheckInDate());
        book.setCheckOutDate(bookingDTO.getCheckOutDate());
        book.setStatus("CONFIRMED");
        bookingRepository.save(book);

    }
}
