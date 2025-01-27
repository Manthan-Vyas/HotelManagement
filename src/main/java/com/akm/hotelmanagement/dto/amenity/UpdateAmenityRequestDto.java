package com.akm.hotelmanagement.dto.amenity;

import com.akm.hotelmanagement.entity.Amenity;
import com.akm.hotelmanagement.validation.NullableNotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Amenity}
 */
@Value
public class UpdateAmenityRequestDto implements Serializable {
    @NullableNotBlank(message = "Name should either be null or not blank")
    @Size(message = "Name must be between 5 and 20 characters long", min = 2, max = 20)
    String name;
    @NullableNotBlank(message = "Description should either be null or not blank")
    @Size(message = "Description must be between 5 and 50 characters long", min = 5, max = 50)
    String description;
}