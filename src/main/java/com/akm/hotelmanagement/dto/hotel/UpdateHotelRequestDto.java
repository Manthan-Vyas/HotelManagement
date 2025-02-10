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

import static com.akm.hotelmanagement.util.Constants.*;

/**
 * DTO for {@link com.akm.hotelmanagement.entity.Hotel}
 */
@Value
public class UpdateHotelRequestDto implements Serializable {
    @NullableNotBlank(message = "{error.nullable.blank.name}")
    @Size(min = HOTEL_NAME_MIN_LENGTH, max = HOTEL_NAME_MAX_LENGTH, message = "{error.hotel.name.size}")
    @Pattern(regexp = NAME_PATTERN, message = "{error.invalid.name.pattern}")
    String name;
    @NullableNotBlank(message = "{error.nullable.blank.address}")
    @Size(min = HOTEL_ADDRESS_MIN_LENGTH, max = HOTEL_ADDRESS_MAX_LENGTH, message = "{error.hotel.address.size}")
    String address;
    @NullableNotBlank(message = "{error.nullable.blank.city}")
    @Size(min = HOTEL_CITY_MIN_LENGTH, max = HOTEL_CITY_MAX_LENGTH, message = "{error.hotel.city.size}")
    String city;
    @NullableNotBlank(message = "{error.nullable.blank.state}")
    @Size(min = HOTEL_STATE_MIN_LENGTH, max = HOTEL_STATE_MAX_LENGTH, message = "{error.hotel.state.size}")
    String state;
    @NullableNotBlank(message = "{error.nullable.blank.zip}")
    @Pattern(regexp = ZIP_PATTERN, message = "{error.invalid.zip.pattern}")
    String zip;
    @NullableNotBlank(message = "{error.nullable.blank.description}")
    @Size(min = HOTEL_DESCRIPTION_MIN_LENGTH, max = HOTEL_DESCRIPTION_MAX_LENGTH, message = "{error.hotel.description.size}")
    String description;
    @Max(value = HOTEL_RATING_MAX_INT, message = "{error.hotel.rating}")
    @PositiveOrZero(message = "{error.hotel.rating}")
    @Nullable
    Double rating;
    @Nullable
    Set<String> imageUrls;
    @NullableNotBlank(message = "{error.nullable.blank.username}")
    String adminUsername;

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