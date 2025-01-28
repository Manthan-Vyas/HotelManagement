package com.akm.hotelmanagement.dto.room;

import com.akm.hotelmanagement.entity.Room;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link Room}
 */
@Value
public class CreateHotelRoomRequestDto implements Serializable {
    @Positive(message = "{error.room.number.positive}")
    int number;
    @Size(message = "{error.room.type.size}", min = 1, max = 50)
    @NotBlank(message = "{error.room.type.required}")
    String type;
    @Size(message = "{error.room.description.size}", min = 5, max = 200)
    @NotBlank(message = "{error.required.description}")
    String description;
    @Positive(message = "{error.room.capacity.positive}")
    int capacity;
    @Positive(message = "{error.room.price.positive}")
    double pricePerNight;
    Set<String> imageUrls;
}