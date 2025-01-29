package com.akm.hotelmanagement.assembler.models;

import com.akm.hotelmanagement.dto.amenity.AmenityResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AmenityModel extends RepresentationModel<AmenityModel> {
    private Long id;
    private String name;
    private String description;
    private Set<HotelModel> hotels;

    public AmenityModel(AmenityResponseDto dto) {
        this(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getHotels().stream().map(HotelModel::new).collect(Collectors.toSet())
        );
    }
}