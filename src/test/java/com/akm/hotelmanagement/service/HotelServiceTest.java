package com.akm.hotelmanagement.service;

import com.akm.hotelmanagement.dto.hotel.CreateHotelRequestDto;
import com.akm.hotelmanagement.dto.hotel.UpdateHotelRequestDto;
import com.akm.hotelmanagement.dto.hotel.HotelResponseDto;
import com.akm.hotelmanagement.entity.Hotel;
import com.akm.hotelmanagement.exception.ResourceAlreadyExistsException;
import com.akm.hotelmanagement.exception.ResourceNotFoundException;
import com.akm.hotelmanagement.repository.HotelRepository;
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    private CreateHotelRequestDto createHotelRequestDto;
    private UpdateHotelRequestDto updateHotelRequestDto;
    private Hotel hotel;

    @BeforeEach
    void setUp() {
        createHotelRequestDto = new CreateHotelRequestDto("Hotel California", "123 Sunset Blvd", "Los Angeles", "CA", "90001", "A lovely place", 5.0, new HashSet<>(Collections.singletonList("image1.jpg")));
        updateHotelRequestDto = new UpdateHotelRequestDto("Hotel California", "123 Sunset Blvd", "Los Angeles", "CA", "90001", "A lovely place", 5.0, new HashSet<>(Collections.singletonList("image1.jpg")));

        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hotel California");
        hotel.setAddress("123 Sunset Blvd");
        hotel.setCity("Los Angeles");
        hotel.setState("CA");
        hotel.setZip("90001");
        hotel.setDescription("A lovely place");
        hotel.setRating(5.0);
        hotel.setImageUrls(new HashSet<>(Collections.singletonList("image1.jpg")));
    }

    @Test
    void testCreateHotel() {
        when(hotelRepository.existsByName(anyString())).thenReturn(false);
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        HotelResponseDto response = hotelService.createHotel(createHotelRequestDto);

        assertNotNull(response);
        assertEquals("Hotel California", response.getName());
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void testCreateHotel_ShouldThrowException_WhenNameExists() {
        when(hotelRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> hotelService.createHotel(createHotelRequestDto));
    }

    @Test
    void testGetHotelById() {
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.of(hotel));

        HotelResponseDto response = hotelService.getHotelById(1L);

        assertNotNull(response);
        assertEquals("Hotel California", response.getName());
    }

    @Test
    void testGetHotelById_ShouldThrowException_WhenHotelNotFound() {
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> hotelService.getHotelById(1L));
    }

    @Test
    void testGetAllHotels() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Hotel> hotelPage = new PageImpl<>(Collections.singletonList(hotel));
        when(hotelRepository.findAll(any(Pageable.class))).thenReturn(hotelPage);

        Page<HotelResponseDto> response = hotelService.getAllHotels(pageable, null, null);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    void testUpdateHotel() {
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        HotelResponseDto response = hotelService.updateHotel(1L, updateHotelRequestDto, true);

        assertNotNull(response);
        assertEquals("Hotel California", response.getName());
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void testUpdateHotel_ShouldThrowException_WhenHotelNotFound() {
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> hotelService.updateHotel(1L, updateHotelRequestDto, true));
    }

    @Test
    void testDeleteHotel() {
        when(hotelRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(hotelRepository).deleteById(anyLong());

        hotelService.deleteHotel(1L);

        verify(hotelRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteHotel_ShouldThrowException_WhenHotelNotFound() {
        when(hotelRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> hotelService.deleteHotel(1L));
    }
}