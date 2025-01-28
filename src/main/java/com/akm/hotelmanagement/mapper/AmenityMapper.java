package com.akm.hotelmanagement.mapper;

import com.akm.hotelmanagement.dto.amenity.AmenityResponseDto;
import com.akm.hotelmanagement.dto.amenity.CreateAmenityRequestDto;
import com.akm.hotelmanagement.dto.amenity.UpdateAmenityRequestDto;
import com.akm.hotelmanagement.entity.Amenity;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

import java.util.stream.Collectors;

public class AmenityMapper {
    public static CreateAmenityRequestDto toCreateDto(@NotNull Amenity amenity) {
        return new CreateAmenityRequestDto(
                amenity.getName(),
                amenity.getDescription()
        );
    }

    public static UpdateAmenityRequestDto toUpdateDto(@NotNull Amenity amenity) {
        return new UpdateAmenityRequestDto(
                amenity.getName(),
                amenity.getDescription()
        );
    }

    public static AmenityResponseDto toResponseDto(@NotNull Amenity amenity) {
        return new AmenityResponseDto(
                amenity.getId(),
                amenity.getName(),
                amenity.getDescription(),
                amenity.getHotels().stream().map(HotelMapper::toResponseDto).collect(Collectors.toSet())
        );
    }

    public static Amenity toEntity(@NotNull CreateAmenityRequestDto createAmenityRequestDto) {
        Amenity amenity = new Amenity();
        amenity.setName(createAmenityRequestDto.getName());
        amenity.setDescription(createAmenityRequestDto.getDescription());
        return amenity;
    }

    public static Amenity toEntity(@NotNull UpdateAmenityRequestDto updateAmenityRequestDto, @Nullable Amenity amenity) {
        if (amenity == null) {
            amenity = new Amenity();
        }
        if (updateAmenityRequestDto.getName() != null) {
            amenity.setName(updateAmenityRequestDto.getName());
        }
        if (updateAmenityRequestDto.getDescription() != null) {
            amenity.setDescription(updateAmenityRequestDto.getDescription());
        }
        return amenity;
    }

    public static Amenity toEntity(@NotNull AmenityResponseDto amenityResponseDto) {
        Amenity amenity = new Amenity();
        amenity.setId(amenityResponseDto.getId());
        amenity.setName(amenityResponseDto.getName());
        amenity.setDescription(amenityResponseDto.getDescription());
        amenity.setHotels(amenityResponseDto.getHotels().stream().map(HotelMapper::toEntity).collect(Collectors.toSet()));
        return amenity;
    }
}
