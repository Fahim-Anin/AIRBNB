package com.example.airbnb.dto;

import java.time.LocalDate;

public class BookingDTO {
    private Long propertyId; // Use Long wrapper
    private LocalDate checkInDate;
    private LocalDate checkOutDate;


    public BookingDTO(LocalDate checkInDate, Long propertyId, LocalDate checkOutDate) {
        this.checkInDate = checkInDate;
        this.propertyId = propertyId;
        this.checkOutDate = checkOutDate;
    }

    public BookingDTO(){

    }

    // Getters and Setters...
    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }
}