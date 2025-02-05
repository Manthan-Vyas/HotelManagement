package com.akm.hotelmanagement.mapper;

import com.akm.hotelmanagement.dto.amenity.AmenityResponseDto;
import com.akm.hotelmanagement.dto.amenity.CreateAmenityRequestDto;
import com.akm.hotelmanagement.dto.amenity.UpdateAmenityRequestDto;
import com.akm.hotelmanagement.entity.Amenity;
import com.akm.hotelmanagement.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AmenityMapperTest {
    private AmenityMapper amenityMapper;

    @BeforeEach
    void setUp() {
        HotelRepository hotelRepository = Mockito.mock(HotelRepository.class);
        amenityMapper = new AmenityMapper(hotelRepository);
    }

    @Test
    void testToCreateDto() {
        Amenity amenity = new Amenity();
        amenity.setName("Pool");
        amenity.setDescription("Outdoor swimming pool");

        CreateAmenityRequestDto dto = amenityMapper.toCreateDto(amenity);

        assertEquals(amenity.getName(), dto.getName());
        assertEquals(amenity.getDescription(), dto.getDescription());
    }

    @Test
    void testToUpdateDto() {
        Amenity amenity = new Amenity();
        amenity.setName("Gym");
        amenity.setDescription("24/7 gym access");

        UpdateAmenityRequestDto dto = amenityMapper.toUpdateDto(amenity);

        assertEquals(amenity.getName(), dto.getName());
        assertEquals(amenity.getDescription(), dto.getDescription());
    }

    @Test
    void testToResponseDto() {
        Amenity amenity = new Amenity();
        amenity.setId(1L);
        amenity.setName("Spa");
        amenity.setDescription("Full-service spa");

        AmenityResponseDto dto = amenityMapper.toResponseDto(amenity);

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

        Amenity amenity = amenityMapper.toEntity(dto);

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

        Amenity updatedAmenity = amenityMapper.toEntity(dto, amenity);

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

        Amenity amenity = amenityMapper.toEntity(dto);

        assertEquals(dto.getId(), amenity.getId());
        assertEquals(dto.getName(), amenity.getName());
        assertEquals(dto.getDescription(), amenity.getDescription());
    }
}