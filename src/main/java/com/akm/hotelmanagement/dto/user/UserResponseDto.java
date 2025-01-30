package com.akm.hotelmanagement.dto.user;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link com.akm.hotelmanagement.entity.User}
 */
@Value
@JsonIdentityInfo(generator = com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserResponseDto implements Serializable {
    UUID id;
    String name;
    String email;
    String username;
    String phone;
    Set<Long> reservationIds;
}