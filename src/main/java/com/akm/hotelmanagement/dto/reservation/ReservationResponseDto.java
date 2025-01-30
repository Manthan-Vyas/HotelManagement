package com.akm.hotelmanagement.dto.reservation;

import com.akm.hotelmanagement.entity.Reservation;
import com.akm.hotelmanagement.entity.util.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Reservation}
 */
@Value
@JsonIdentityInfo(generator = com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ReservationResponseDto implements Serializable {
    Long id;
    LocalDate checkIn;
    LocalDate checkOut;
    int numberOfGuests;
    double totalPrice;
    LocalDate reservationDate;
    ReservationStatus status;
    String username;
    Long roomId;
}