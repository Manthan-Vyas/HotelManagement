package com.akm.hotelmanagement.dto.hotel;

import com.akm.hotelmanagement.validation.NullableNotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
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
    @NullableNotBlank(message = "{error.nullable.blank.name}")
    @Size(min = 2, max = 50, message = "{error.hotel.name.size}")
    String name;
    @NullableNotBlank(message = "{error.nullable.blank.address}")
    @Size(min = 5, max = 100, message = "{error.hotel.address.size}")
    String address;
    @NullableNotBlank(message = "{error.nullable.blank.city}")
    @Size(min = 2, max = 50, message = "{error.hotel.city.size}")
    String city;
    @NullableNotBlank(message = "{error.nullable.blank.state}")
    @Size(min = 2, max = 50, message = "{error.hotel.state.size}")
    String state;
    @NullableNotBlank(message = "{error.nullable.blank.zip}")
    @Pattern(regexp = "^\\d{6}$", message = "{error.invalid.zip.pattern}")
    String zip;
    @NullableNotBlank(message = "{error.nullable.blank.description}")
    @Size(min = 5, max = 200, message = "{error.hotel.description.size}")
    String description;
    @Max(value = 5, message = "{error.hotel.rating}")
    @PositiveOrZero(message = "{error.hotel.rating}")
    @Nullable
    Double rating;
    @Nullable
    Set<String> imageUrls;

    public boolean hasAllFieldsNull() {
        return this.getName() == null && this.getAddress() == null && this.getCity() == null && this.getState() == null && this.getZip() == null && this.getDescription() == null && this.getRating() == null && this.getImageUrls() == null;
    }

    public boolean hasAllFieldsNotNull() {
        return this.getName() != null && this.getAddress() != null && this.getCity() != null && this.getState() != null && this.getZip() != null && this.getDescription() != null && this.getRating() != null && this.getImageUrls() != null;
    }

    public boolean hasAnyFieldNotNull() {
        return this.getName() != null || this.getAddress() != null || this.getCity() != null || this.getState() != null || this.getZip() != null || this.getDescription() != null || this.getRating() != null || this.getImageUrls() != null;
    }

    public boolean hasAnyFieldNull() {
        return this.getName() == null || this.getAddress() == null || this.getCity() == null || this.getState() == null || this.getZip() == null || this.getDescription() == null || this.getRating() == null || this.getImageUrls() == null;
    }
}