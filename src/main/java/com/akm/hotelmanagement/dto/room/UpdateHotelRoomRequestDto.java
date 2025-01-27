package com.akm.hotelmanagement.dto.room;

import com.akm.hotelmanagement.entity.Room;
import com.akm.hotelmanagement.entity.util.RoomStatus;
import com.akm.hotelmanagement.validation.NullableNotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link Room}
 */
@Value
public class UpdateHotelRoomRequestDto implements Serializable {
    @Positive(message = "Room number must be a positive integer")
    @Nullable
    Integer number;
    @Size(message = "Room type must be between 1 and 255 characters long", min = 1, max = 255)
    @NullableNotBlank(message = "Room should either be null or not blank")
    String type;
    @Size(message = "Room description must be between 5 and 255 characters long", min = 5, max = 255)
    @NullableNotBlank(message = "Room description is mandatory")
    String description;
    @Positive(message = "Number of beds must be a positive integer")
    @Nullable
    Integer numberOfBeds;
    @Positive(message = "Price of the room per night must be a positive integer")
    @Nullable
    Double pricePerNight;
    @Nullable
    RoomStatus roomStatus; // todo: check if this is validated as proper RoomStatus
    Set<String> imageUrls;
}