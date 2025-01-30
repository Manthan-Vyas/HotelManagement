package com.akm.hotelmanagement.mapper;

import com.akm.hotelmanagement.dto.amenity.AmenityResponseDto;
import com.akm.hotelmanagement.dto.amenity.CreateAmenityRequestDto;
import com.akm.hotelmanagement.dto.amenity.UpdateAmenityRequestDto;
import com.akm.hotelmanagement.entity.Amenity;
import com.akm.hotelmanagement.entity.Hotel;
import com.akm.hotelmanagement.repository.HotelRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AmenityMapper {

    private final HotelRepository hotelRepository;

    public CreateAmenityRequestDto toCreateDto(@NotNull Amenity amenity) {
        return new CreateAmenityRequestDto(
                amenity.getName(),
                amenity.getDescription()
        );
    }

    public UpdateAmenityRequestDto toUpdateDto(@NotNull Amenity amenity) {
        return new UpdateAmenityRequestDto(
                amenity.getName(),
                amenity.getDescription()
        );
    }

    public AmenityResponseDto toResponseDto(@NotNull Amenity amenity) {
        return new AmenityResponseDto(
                amenity.getId(),
                amenity.getName(),
                amenity.getDescription(),
                amenity.getHotels().stream().map(Hotel::getId).collect(Collectors.toSet())
        );
    }

    public Amenity toEntity(@NotNull CreateAmenityRequestDto createAmenityRequestDto) {
        Amenity amenity = new Amenity();
        amenity.setName(createAmenityRequestDto.getName());
        amenity.setDescription(createAmenityRequestDto.getDescription());
        return amenity;
    }

    public Amenity toEntity(@NotNull UpdateAmenityRequestDto updateAmenityRequestDto, @Nullable Amenity amenity) {
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

    public Amenity toEntity(@NotNull AmenityResponseDto amenityResponseDto) {
        Amenity amenity = new Amenity();
        amenity.setId(amenityResponseDto.getId());
        amenity.setName(amenityResponseDto.getName());
        amenity.setDescription(amenityResponseDto.getDescription());
        Set<Hotel> hotels = amenityResponseDto.getHotelIds().stream().map(hotelRepository::findById).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());
        amenity.setHotels(hotels);
        return amenity;
    }
}
