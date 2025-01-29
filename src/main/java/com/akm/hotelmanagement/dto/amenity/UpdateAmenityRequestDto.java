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
    @NullableNotBlank(message = "{error.nullable.blank.name}")
    @Size(message = "{error.amenity.name.size}", min = 2, max = 20)
    String name;
    @NullableNotBlank(message = "{error.nullable.blank.description}")
    @Size(message = "{error.amenity.description.size}", min = 5, max = 50)
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