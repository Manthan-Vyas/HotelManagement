package com.akm.hotelmanagement.assembler.models;

import com.akm.hotelmanagement.dto.room.RoomResponseDto;
import com.akm.hotelmanagement.entity.util.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomModel extends RepresentationModel<RoomModel> {
    private Long id;
    private int number;
    private String type;
    private String description;
    private int capacity;
    private double pricePerNight;
    private RoomStatus status;
    private Set<String> imageUrls;

    public RoomModel(RoomResponseDto dto) {
        this(
                dto.getId(),
                dto.getNumber(),
                dto.getType(),
                dto.getDescription(),
                dto.getCapacity(),
                dto.getPricePerNight(),
                dto.getStatus(),
                dto.getImageUrls()
        );
    }
}