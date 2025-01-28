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
    @NotBlank(message = "{error.required.name}")
    @Size(message = "{error.amenity.name.size}", min = 2, max = 20)
    String name;
    @NotBlank(message = "{error.required.description}")
    @Size(message = "{error.amenity.description.size}", min = 5, max = 50)
    String description;
}