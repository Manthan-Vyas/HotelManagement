package com.akm.hotelmanagement.dto.reservation;

import com.akm.hotelmanagement.dto.room.RoomResponseDto;
import com.akm.hotelmanagement.dto.user.UserResponseDto;
import com.akm.hotelmanagement.entity.Reservation;
import com.akm.hotelmanagement.entity.util.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Reservation}
 */
@Value
public class ReservationResponseDto implements Serializable {
    Long id;
    LocalDate checkIn;
    LocalDate checkOut;
    int numberOfGuests;
    double totalPrice;
    LocalDate reservationDate;
    ReservationStatus status;
    @JsonBackReference
    UserResponseDto user;
    @JsonBackReference
    RoomResponseDto room;
}