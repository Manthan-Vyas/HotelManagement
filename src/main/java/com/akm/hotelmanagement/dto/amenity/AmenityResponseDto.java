package com.akm.hotelmanagement.dto.amenity;

import com.akm.hotelmanagement.dto.hotel.HotelResponseDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    Set<HotelResponseDto> hotels;
}