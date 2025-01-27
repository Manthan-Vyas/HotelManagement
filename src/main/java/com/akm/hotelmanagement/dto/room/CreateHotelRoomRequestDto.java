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
    @Positive(message = "Room number must be a positive integer")
    int number;
    @Size(message = "Room type must be between 1 and 255 characters long", min = 1, max = 255)
    @NotBlank(message = "Room type is mandatory")
    String type;
    @Size(message = "Room description must be between 5 and 255 characters long", min = 5, max = 255)
    @NotBlank(message = "Room description is mandatory")
    String description;
    @Positive(message = "Number of beds must be a positive integer")
    int numberOfBeds;
    @Positive(message = "Price of the room per night must be a positive integer")
    double pricePerNight;
    Set<String> imageUrls;
}