package com.akm.hotelmanagement.dto.user;

import com.akm.hotelmanagement.dto.reservation.ReservationResponseDto;
import com.akm.hotelmanagement.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link com.akm.hotelmanagement.entity.User}
 */
@Value
public class UserResponseDto implements Serializable {
    UUID id;
    String name;
    String email;
    String username;
    String phone;
    @JsonManagedReference
    Set<ReservationResponseDto> reservations;
}