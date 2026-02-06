package com.example.airbnb.repository;

import com.example.airbnb.model.Book;
import com.example.airbnb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;


public interface BookingRepository extends JpaRepository<Book,Long>{
    boolean existsBookByUserEmailAndCheckInDateCheckOutDate(User userEmail, LocalDate checkInDate, LocalDate checkOutDate);
    boolean existsByCheckInDate(LocalDate checkInDate);

}
