package com.akm.hotelmanagement.dto.hotel;

import com.akm.hotelmanagement.validation.NullableNotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.akm.hotelmanagement.entity.Hotel}
 */
@Value
public class UpdateHotelRequestDto implements Serializable {
    @NullableNotBlank(message = "Name should either be null or not blank")
    @Size(min = 5, max = 20, message = "Name must be between 5 and 20 characters long")
    String name;
    @NullableNotBlank(message = "Address should either be null or not blank")
    @Size(min = 5, max = 20, message = "Address must be between 5 and 20 characters long")
    String address;
    @NullableNotBlank(message = "City should either be null or not blank")
    @Size(min = 5, max = 20, message = "City must be between 5 and 20 characters long")
    String city;
    @NullableNotBlank(message = "State should either be null or not blank")
    @Size(min = 5, max = 20, message = "State must be between 5 and 20 characters long")
    String state;
    @NullableNotBlank(message = "Country should either be null or not blank")
    @Size(min = 5, max = 20, message = "Country must be between 5 and 20 characters long")
    String zip;
    @NullableNotBlank(message = "Description should either be null or not blank")
    @Size(min = 5, max = 20, message = "Description must be between 5 and 20 characters long")
    String description;
    @Max(value = 5, message = "Rating must be less than or equal to 5")
    @PositiveOrZero(message = "Rating must be greater than or equal to 0")
    @Nullable
    Double rating;
    @Nullable
    Set<String> imageUrls;
}