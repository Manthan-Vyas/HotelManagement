package com.akm.hotelmanagement.dto.amenity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

import static com.akm.hotelmanagement.util.Constants.*;

/**
 * DTO for {@link com.akm.hotelmanagement.entity.Amenity}
 */
@Value
public class CreateAmenityRequestDto implements Serializable {
    @NotBlank(message = "{error.required.name}")
    @Size(message = "{error.amenity.name.size}", min = AMENITY_NAME_MIN_LENGTH, max = AMENITY_NAME_MAX_LENGTH)
    @Pattern(regexp = NAME_PATTERN, message = "{error.invalid.name.pattern}")
    String name;
    @NotBlank(message = "{error.required.description}")
    @Size(message = "{error.amenity.description.size}", min = AMENITY_DESCRIPTION_MIN_LENGTH, max = AMENITY_DESCRIPTION_MAX_LENGTH)
    String description;
}