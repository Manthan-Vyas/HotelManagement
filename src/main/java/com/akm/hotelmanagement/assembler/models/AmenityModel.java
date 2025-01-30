package com.akm.hotelmanagement.assembler.models;

import com.akm.hotelmanagement.dto.amenity.AmenityResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AmenityModel extends RepresentationModel<AmenityModel> {
    private Long id;
    private String name;
    private String description;

    public AmenityModel(AmenityResponseDto dto) {
        this(
                dto.getId(),
                dto.getName(),
                dto.getDescription()
        );
    }
}