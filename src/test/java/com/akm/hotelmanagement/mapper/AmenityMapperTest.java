package com.akm.hotelmanagement.mapper;

import com.akm.hotelmanagement.dto.amenity.AmenityResponseDto;
import com.akm.hotelmanagement.dto.amenity.CreateAmenityRequestDto;
import com.akm.hotelmanagement.dto.amenity.UpdateAmenityRequestDto;
import com.akm.hotelmanagement.entity.Amenity;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AmenityMapperTest {

    @Test
    void testToCreateDto() {
        Amenity amenity = new Amenity();
        amenity.setName("Pool");
        amenity.setDescription("Outdoor swimming pool");

        CreateAmenityRequestDto dto = AmenityMapper.toCreateDto(amenity);

        assertEquals(amenity.getName(), dto.getName());
        assertEquals(amenity.getDescription(), dto.getDescription());
    }

    @Test
    void testToUpdateDto() {
        Amenity amenity = new Amenity();
        amenity.setName("Gym");
        amenity.setDescription("24/7 gym access");

        UpdateAmenityRequestDto dto = AmenityMapper.toUpdateDto(amenity);

        assertEquals(amenity.getName(), dto.getName());
        assertEquals(amenity.getDescription(), dto.getDescription());
    }

    @Test
    void testToResponseDto() {
        Amenity amenity = new Amenity();
        amenity.setId(1L);
        amenity.setName("Spa");
        amenity.setDescription("Full-service spa");

        AmenityResponseDto dto = AmenityMapper.toResponseDto(amenity);

        assertEquals(amenity.getId(), dto.getId());
        assertEquals(amenity.getName(), dto.getName());
        assertEquals(amenity.getDescription(), dto.getDescription());
    }

    @Test
    void testToEntityFromCreateDto() {
        CreateAmenityRequestDto dto = new CreateAmenityRequestDto(
                "Restaurant",
                "Fine dining restaurant"
        );

        Amenity amenity = AmenityMapper.toEntity(dto);

        assertEquals(dto.getName(), amenity.getName());
        assertEquals(dto.getDescription(), amenity.getDescription());
    }

    @Test
    void testToEntityFromUpdateDto() {
        UpdateAmenityRequestDto dto = new UpdateAmenityRequestDto(
                "Bar",
                "Rooftop bar"
        );

        Amenity amenity = new Amenity();
        amenity.setName("Old Bar");
        amenity.setDescription("Old description");

        Amenity updatedAmenity = AmenityMapper.toEntity(dto, amenity);

        assertEquals(dto.getName(), updatedAmenity.getName());
        assertEquals(dto.getDescription(), updatedAmenity.getDescription());
    }

    @Test
    void testToEntityFromResponseDto() {
        AmenityResponseDto dto = new AmenityResponseDto(
                1L,
                "Cafe",
                "Coffee shop",
                new HashSet<>()
        );

        Amenity amenity = AmenityMapper.toEntity(dto);

        assertEquals(dto.getId(), amenity.getId());
        assertEquals(dto.getName(), amenity.getName());
        assertEquals(dto.getDescription(), amenity.getDescription());
    }
}