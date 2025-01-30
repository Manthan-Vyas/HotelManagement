package com.akm.hotelmanagement.dto.hotel;

import com.akm.hotelmanagement.entity.Hotel;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link Hotel}
 */
@Value
@JsonIdentityInfo(generator = com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
    Set<Long> roomIds;
    Set<Long> amenityIds;
}