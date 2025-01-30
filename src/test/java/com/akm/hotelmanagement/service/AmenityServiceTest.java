package com.akm.hotelmanagement.service;

import com.akm.hotelmanagement.dto.amenity.AmenityResponseDto;
import com.akm.hotelmanagement.dto.amenity.CreateAmenityRequestDto;
import com.akm.hotelmanagement.dto.amenity.UpdateAmenityRequestDto;
import com.akm.hotelmanagement.entity.Amenity;
import com.akm.hotelmanagement.exception.ResourceNotFoundException;
import com.akm.hotelmanagement.mapper.AmenityMapper;
import com.akm.hotelmanagement.repository.AmenityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AmenityServiceTest {

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private AmenityMapper amenityMapper;

    @InjectMocks
    private AmenityService amenityService;

    private CreateAmenityRequestDto createAmenityRequestDto;
    private UpdateAmenityRequestDto updateAmenityRequestDto;
    private Amenity amenity;

    @BeforeEach
    void setUp() {
        createAmenityRequestDto = new CreateAmenityRequestDto("Pool", "Swimming pool");
        updateAmenityRequestDto = new UpdateAmenityRequestDto("Gym", "Fitness center");

        amenity = new Amenity();
        amenity.setId(1L);
        amenity.setName("Pool");
        amenity.setDescription("Swimming pool");
    }

    @Test
    void testCreateAmenity() {
        when(amenityRepository.existsByName(anyString())).thenReturn(false);
        when(amenityMapper.toEntity(any(CreateAmenityRequestDto.class))).thenReturn(amenity); // Mocking amenityMapper.toEntity
        when(amenityRepository.save(any(Amenity.class))).thenReturn(amenity);
        when(amenityMapper.toResponseDto(any(Amenity.class))).thenReturn(new AmenityResponseDto(1L, "Pool", "Swimming pool", new HashSet<>(List.of(1L))));

        AmenityResponseDto response = amenityService.createAmenity(createAmenityRequestDto);

        assertNotNull(response);
        assertEquals("Pool", response.getName());
        verify(amenityRepository, times(1)).save(any(Amenity.class));
    }

    @Test
    void testCreateAmenity_ShouldThrowException_WhenNameExists() {
        when(amenityRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> amenityService.createAmenity(createAmenityRequestDto));
    }

    @Test
    void testGetAmenityById() {
        when(amenityRepository.findById(anyLong())).thenReturn(Optional.of(amenity));
        when(amenityMapper.toResponseDto(any(Amenity.class))).thenReturn(new AmenityResponseDto(1L, "Pool", "Swimming pool", new HashSet<>()));

        AmenityResponseDto response = amenityService.getAmenityById(1L);

        assertNotNull(response);
        assertEquals("Pool", response.getName());
    }

    @Test
    void testGetAmenityById_ShouldThrowException_WhenAmenityNotFound() {
        when(amenityRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> amenityService.getAmenityById(1L));
    }

    @Test
    void testGetAllAmenities() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Amenity> amenityPage = new PageImpl<>(Collections.singletonList(amenity));
        when(amenityRepository.findAll(any(Pageable.class))).thenReturn(amenityPage);
        when(amenityMapper.toResponseDto(any(Amenity.class))).thenReturn(new AmenityResponseDto(1L, "Pool", "Swimming pool", new HashSet<>()));

        Page<AmenityResponseDto> response = amenityService.getAllAmenities(pageable, null, null);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    void testUpdateAmenity() {
        when(amenityRepository.findById(anyLong())).thenReturn(Optional.of(amenity));
        when(amenityRepository.save(any(Amenity.class))).thenReturn(amenity);
        when(amenityMapper.toResponseDto(any(Amenity.class))).thenReturn(new AmenityResponseDto(1L, "Gym", "Fitness center", new HashSet<>()));

        AmenityResponseDto response = amenityService.updateAmenity(1L, updateAmenityRequestDto, true);

        assertNotNull(response);
        assertEquals("Gym", response.getName());
        verify(amenityRepository, times(1)).save(any(Amenity.class));
    }

    @Test
    void testUpdateAmenity_ShouldThrowException_WhenAmenityNotFound() {
        when(amenityRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> amenityService.updateAmenity(1L, updateAmenityRequestDto, true));
    }

    @Test
    void testDeleteAmenity() {
        when(amenityRepository.findById(anyLong())).thenReturn(Optional.of(amenity));
        doNothing().when(amenityRepository).delete(any(Amenity.class));

        amenityService.deleteAmenity(1L);

        verify(amenityRepository, times(1)).delete(any(Amenity.class));
    }

    @Test
    void testDeleteAmenity_ShouldThrowException_WhenAmenityNotFound() {
        when(amenityRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> amenityService.deleteAmenity(1L));
    }
}