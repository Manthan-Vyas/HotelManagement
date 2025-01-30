package com.akm.hotelmanagement.assembler.models;

import com.akm.hotelmanagement.dto.hotel.HotelResponseDto;
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
public class HotelModel extends RepresentationModel<HotelModel> {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String description;
    private double rating;
    private Set<String> imageUrls;

    public HotelModel(HotelResponseDto dto) {
        this(
            dto.getId(),
            dto.getName(),
            dto.getAddress(),
            dto.getCity(),
            dto.getState(),
            dto.getZip(),
            dto.getDescription(),
            dto.getRating(),
            dto.getImageUrls()
        );
    }
}