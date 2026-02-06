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
    private User userEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status;

    public Book(User userEmail, Property property, LocalDate checkInDate, LocalDate checkOutDate, String status) {
        this.userEmail = userEmail;
        this.property = property;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
    }

    public Book() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserEmail(User userEmail) {
        this.userEmail = userEmail;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Property getProperty() {
        return property;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public String getStatus() {
        return status;
    }

    public User getUserEmail() {
        return userEmail;
    }

    public Long getId() {
        return id;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
