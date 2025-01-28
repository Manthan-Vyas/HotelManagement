package com.akm.hotelmanagement.dto.room;

import com.akm.hotelmanagement.dto.hotel.HotelResponseDto;
import com.akm.hotelmanagement.dto.reservation.ReservationResponseDto;
import com.akm.hotelmanagement.entity.Room;
import com.akm.hotelmanagement.entity.util.RoomStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link Room}
 */
@Value
public class RoomResponseDto implements Serializable {
    Long id;
    int number;
    String type;
    String description;
    int capacity;
    double pricePerNight;
    RoomStatus status;
    Set<String> imageUrls;
    @JsonBackReference
    HotelResponseDto hotel;
    @JsonManagedReference
    Set<ReservationResponseDto> reservations;
}