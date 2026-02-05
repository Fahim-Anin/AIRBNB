package com.example.airbnb.dto;

import java.time.LocalDate;

public class BookingDTO {



    private String property_id;

    private LocalDate  checkInDate;
    private LocalDate checkOutDate;

    private String status;

    public BookingDTO() {

    }

    public BookingDTO(LocalDate checkInDate, String property_id, LocalDate checkOutDate, String status) {
        this.checkInDate = checkInDate;
        this.property_id = property_id;
        this.checkOutDate = checkOutDate;
        this.status = status;
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

    public String getProperty_id() {
        return property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }
}
