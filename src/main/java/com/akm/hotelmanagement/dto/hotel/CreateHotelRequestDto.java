package com.akm.hotelmanagement.dto.hotel;

import jakarta.validation.constraints.*;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.akm.hotelmanagement.entity.Hotel}
 */
@Value
public class CreateHotelRequestDto implements Serializable {
    @Size(min = 2, max = 50, message = "{error.hotel.name.size}")
    @NotBlank(message = "{error.required.name}")
    String name;
    @Size(min = 5, max = 100, message = "{error.hotel.address.size}")
    @NotBlank(message = "{error.required.address}")
    String address;
    @Size(min = 2, max = 50, message = "{error.hotel.city.size}")
    @NotBlank(message = "{error.required.city}")
    String city;
    @Size(min = 2, max = 50, message = "{error.hotel.state.size}")
    @NotBlank(message = "{error.required.state}")
    String state;
    @Pattern(regexp = "^\\d{6}$", message = "{error.invalid.zip.pattern}")
    @NotBlank(message = "{error.required.zip}")
    String zip;
    @Size(min = 5, max = 200, message = "{error.hotel.description.size}")
    @NotBlank(message = "{error.required.description}")
    String description;
    @Max(value = 5, message = "{error.hotel.rating}")
    @PositiveOrZero(message = "{error.hotel.rating}")
    double rating;
    Set<String> imageUrls;
}