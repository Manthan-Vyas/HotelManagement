package com.akm.hotelmanagement.dto.reservation;

import jakarta.validation.constraints.*;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.akm.hotelmanagement.entity.Reservation}
 */
@Value
public class CreateOrUpdateUserRoomReservationRequestDto implements Serializable {
    @FutureOrPresent(message = "{error.reservation.check-in.before-today}")
    LocalDate checkIn;
    @Future(message = "{error.reservation.check-out.before-check-in}")
    LocalDate checkOut;
    @Positive(message = "{error.reservation.number-of-guests}")
    int numberOfGuests;
}