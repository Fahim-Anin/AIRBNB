package com.example.airbnb.model;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name ="Booking")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_email", nullable = false)
    private User user_email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status;

    public Book(User user_email, Property property, Long id, LocalDate checkInDate, LocalDate checkOutDate, String status) {
        this.user_email = user_email;
        this.property = property;
        this.id = id;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
    }

    public Book() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser_email() {
        return user_email;
    }

    public void setUser_email(User user_email) {
        this.user_email = user_email;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
