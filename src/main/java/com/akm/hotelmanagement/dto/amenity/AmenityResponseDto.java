package com.akm.hotelmanagement.dto.amenity;

import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.akm.hotelmanagement.entity.Amenity}
 */
@Value
public class AmenityResponseDto implements Serializable {
    Long id;
    String name;
    String description;
    Set<Long> hotelIds;
}