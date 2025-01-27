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
    @FutureOrPresent(message = "Invalid check-in date")
    LocalDate checkIn;
    @Future(message = "Invalid check-out date")
    LocalDate checkOut;
    @Positive(message = "Invalid number of guests")
    int numberOfGuests;
}