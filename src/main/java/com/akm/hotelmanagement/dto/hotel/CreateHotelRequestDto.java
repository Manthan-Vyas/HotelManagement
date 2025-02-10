package com.akm.hotelmanagement.dto.hotel;

import jakarta.validation.constraints.*;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

import static com.akm.hotelmanagement.util.Constants.*;

/**
 * DTO for {@link com.akm.hotelmanagement.entity.Hotel}
 */
@Value
public class CreateHotelRequestDto implements Serializable {
    @Size(min = HOTEL_NAME_MIN_LENGTH, max = HOTEL_NAME_MAX_LENGTH, message = "{error.hotel.name.size}")
    @NotBlank(message = "{error.required.name}")
    @Pattern(regexp = NAME_PATTERN, message = "{error.invalid.name.pattern}")
    String name;
    @Size(min = HOTEL_ADDRESS_MIN_LENGTH, max = HOTEL_ADDRESS_MAX_LENGTH, message = "{error.hotel.address.size}")
    @NotBlank(message = "{error.required.address}")
    String address;
    @Size(min = HOTEL_CITY_MIN_LENGTH, max = HOTEL_CITY_MAX_LENGTH, message = "{error.hotel.city.size}")
    @NotBlank(message = "{error.required.city}")
    String city;
    @Size(min = HOTEL_STATE_MIN_LENGTH, max = HOTEL_STATE_MAX_LENGTH, message = "{error.hotel.state.size}")
    @NotBlank(message = "{error.required.state}")
    String state;
    @Pattern(regexp = ZIP_PATTERN, message = "{error.invalid.zip.pattern}")
    @NotBlank(message = "{error.required.zip}")
    String zip;
    @Size(min = HOTEL_DESCRIPTION_MIN_LENGTH, max = HOTEL_DESCRIPTION_MAX_LENGTH, message = "{error.hotel.description.size}")
    @NotBlank(message = "{error.required.description}")
    String description;
    @Max(value = HOTEL_RATING_MAX_INT, message = "{error.hotel.rating}")
    @PositiveOrZero(message = "{error.hotel.rating}")
    double rating;
    Set<String> imageUrls;
    @NotBlank(message = "{error.required.username}")
    String adminUsername;
}