package com.akm.hotelmanagement.dto.hotel;

import com.akm.hotelmanagement.dto.amenity.AmenityResponseDto;
import com.akm.hotelmanagement.dto.room.RoomResponseDto;
import com.akm.hotelmanagement.entity.Hotel;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link Hotel}
 */
@Value
public class HotelResponseDto implements Serializable {
    Long id;
    String name;
    String address;
    String city;
    String state;
    String zip;
    String description;
    double rating;
    Set<String> imageUrls;
    @JsonManagedReference
    Set<RoomResponseDto> rooms;
    @JsonManagedReference
    Set<AmenityResponseDto> amenities;
}