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

import static com.akm.hotelmanagement.util.Constants.*;

/**
 * DTO for {@link Room}
 */
@Value
public class UpdateHotelRoomRequestDto implements Serializable {
    @Positive(message = "{error.room.number.positive}")
    @Nullable
    Integer number;
    @Size(message = "{error.room.type.size}", min = ROOM_TYPE_MIN_LENGTH, max = ROOM_TYPE_MAX_LENGTH)
    @NullableNotBlank(message = "{error.room.type.nullable.blank}")
    String type;
    @Size(message = "{error.room.description.size}", min = ROOM_DESCRIPTION_MIN_LENGTH, max = ROOM_DESCRIPTION_MAX_LENGTH)
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

    public boolean hasAllFieldsNull() {
        return this.getNumber() == null && this.getType() == null && this.getDescription() == null && this.getCapacity() == null && this.getPricePerNight() == null && this.getRoomStatus() == null && this.getImageUrls() == null;
    }

    public boolean hasAnyFieldNotNull() {
        return this.getNumber() != null || this.getType() != null || this.getDescription() != null || this.getCapacity() != null || this.getPricePerNight() != null || this.getRoomStatus() != null || this.getImageUrls() != null;
    }

    public boolean hasAnyFieldNull() {
        return this.getNumber() == null || this.getType() == null || this.getDescription() == null || this.getCapacity() == null || this.getPricePerNight() == null || this.getRoomStatus() == null || this.getImageUrls() == null;
    }

    public boolean hasAllFieldsNotNull() {
        return this.getNumber() != null && this.getType() != null && this.getDescription() != null && this.getCapacity() != null && this.getPricePerNight() != null && this.getRoomStatus() != null && this.getImageUrls() != null;
    }
}