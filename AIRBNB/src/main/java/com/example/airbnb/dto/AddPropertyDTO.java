package com.example.airbnb.dto;

public class AddPropertyDTO {
    private String propertyName;
    private int propertyValue;

    public AddPropertyDTO() {
    }
    public AddPropertyDTO(String propertyName, int propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public int getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(int propertyValue) {
        this.propertyValue = propertyValue;
    }
}