package com.akm.hotelmanagement.dto.hotel;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.akm.hotelmanagement.entity.Hotel}
 */
@Value
public class CreateHotelRequestDto implements Serializable {
    @Size(min = 5, max = 20, message = "Name must be between 5 and 20 characters long")
    @NotBlank(message = "Name is mandatory")
    String name;
    @Size(min = 5, max = 20, message = "Address must be between 5 and 20 characters long")
    @NotBlank(message = "Address is mandatory")
    String address;
    @Size(min = 5, max = 20, message = "City must be between 5 and 20 characters long")
    @NotBlank(message = "City is mandatory")
    String city;
    @Size(min = 5, max = 20, message = "State must be between 5 and 20 characters long")
    @NotBlank(message = "State is mandatory")
    String state;
    @Size(min = 5, max = 20, message = "Country must be between 5 and 20 characters long")
    @NotBlank(message = "Country is mandatory")
    String zip;
    @Size(min = 5, max = 20, message = "Description must be between 5 and 20 characters long")
    @NotBlank(message = "Description is mandatory")
    String description;
    @Max(value = 5, message = "Rating must be less than or equal to 5")
    @PositiveOrZero(message = "Rating must be greater than or equal to 0")
    double rating;
    Set<String> imageUrls;
}