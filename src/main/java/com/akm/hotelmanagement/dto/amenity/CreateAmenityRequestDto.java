package com.akm.hotelmanagement.dto.amenity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.akm.hotelmanagement.entity.Amenity}
 */
@Value
public class CreateAmenityRequestDto implements Serializable {
    @NotBlank(message = "Name is mandatory")
    @Size(message = "Name must be between 5 and 20 characters long", min = 2, max = 20)
    String name;
    @NotBlank(message = "Description is mandatory")
    @Size(message = "Description must be between 5 and 50 characters long", min = 5, max = 50)
    String description;
}