package com.akm.hotelmanagement.dto.room;

import com.akm.hotelmanagement.entity.Room;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

import static com.akm.hotelmanagement.util.Constants.*;

/**
 * DTO for {@link Room}
 */
@Value
public class CreateHotelRoomRequestDto implements Serializable {
    @Positive(message = "{error.room.number.positive}")
    int number;
    @Size(message = "{error.room.type.size}", min = ROOM_TYPE_MIN_LENGTH, max = ROOM_TYPE_MAX_LENGTH)
    @NotBlank(message = "{error.room.type.required}")
    String type;
    @Size(message = "{error.room.description.size}", min = ROOM_DESCRIPTION_MIN_LENGTH, max = ROOM_DESCRIPTION_MAX_LENGTH)
    @NotBlank(message = "{error.required.description}")
    String description;
    @Positive(message = "{error.room.capacity.positive}")
    int capacity;
    @Positive(message = "{error.room.price.positive}")
    double pricePerNight;
    Set<String> imageUrls;
}