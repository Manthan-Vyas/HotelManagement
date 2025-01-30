package com.akm.hotelmanagement.dto.room;

import com.akm.hotelmanagement.entity.Room;
import com.akm.hotelmanagement.entity.util.RoomStatus;
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
    Long hotelId;
    Set<Long> reservationIds;
}