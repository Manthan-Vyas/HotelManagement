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
    @Positive(message = "{error.room.number.positive}")
    @Nullable
    Integer number;
    @Size(message = "{error.room.type.size}", min = 1, max = 50)
    @NullableNotBlank(message = "{error.room.type.nullable.blank}")
    String type;
    @Size(message = "{error.room.description.size}", min = 5, max = 200)
    @NullableNotBlank(message = "{error.nullable.blank.description.}")
    String description;
    @Positive(message = "{error.room.capacity.positive}")
    @Nullable
    Integer capacity;
    @Positive(message = "{error.room.price.positive}")
    @Nullable
    Double pricePerNight;
    @Nullable
    RoomStatus roomStatus; // todo: check if this is validated as proper RoomStatus
    Set<String> imageUrls;
}