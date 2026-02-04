package com.example.airbnb.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class AddPropertyDTO {
    @NotBlank(message = "Property name cannot be empty")
    private String propertyName;

    @Min(value = 1, message = "Price must be greater than zero")
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