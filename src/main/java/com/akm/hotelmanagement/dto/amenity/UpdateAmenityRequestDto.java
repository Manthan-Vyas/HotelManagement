package com.akm.hotelmanagement.dto.amenity;

import com.akm.hotelmanagement.entity.Amenity;
import com.akm.hotelmanagement.validation.NullableNotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

import static com.akm.hotelmanagement.util.Constants.*;

/**
 * DTO for {@link Amenity}
 */
@Value
public class UpdateAmenityRequestDto implements Serializable {
    @NullableNotBlank(message = "{error.nullable.blank.name}")
    @Size(message = "{error.amenity.name.size}", min = AMENITY_NAME_MIN_LENGTH, max = AMENITY_NAME_MAX_LENGTH)
    @Pattern(regexp = NAME_PATTERN, message = "{error.invalid.name.pattern}")
    String name;
    @NullableNotBlank(message = "{error.nullable.blank.description}")
    @Size(message = "{error.amenity.description.size}", min = AMENITY_DESCRIPTION_MIN_LENGTH, max = AMENITY_DESCRIPTION_MAX_LENGTH)
    String description;

    public boolean hasAllFieldsNull() {
        return this.getName() == null && this.getDescription() == null;
    }

    public boolean hasAllFieldsNotNull() {
        return this.getName() != null && this.getDescription() != null;
    }

    public boolean hasAnyFieldNull() {
        return this.getName() == null || this.getDescription() == null;
    }

    public boolean hasAnyFieldNotNull() {
        return this.getName() != null || this.getDescription() != null;
    }
}