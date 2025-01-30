package com.akm.hotelmanagement.service;

import com.akm.hotelmanagement.dto.room.CreateHotelRoomRequestDto;
import com.akm.hotelmanagement.dto.room.UpdateHotelRoomRequestDto;
import com.akm.hotelmanagement.dto.room.RoomResponseDto;
import com.akm.hotelmanagement.entity.Hotel;
import com.akm.hotelmanagement.entity.Room;
import com.akm.hotelmanagement.entity.util.RoomStatus;
import com.akm.hotelmanagement.exception.ResourceAlreadyExistsException;
import com.akm.hotelmanagement.exception.ResourceNotFoundException;
import com.akm.hotelmanagement.repository.HotelRepository;
import com.akm.hotelmanagement.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private RoomService roomService;

    private CreateHotelRoomRequestDto createRoomRequestDto;
    private UpdateHotelRoomRequestDto updateRoomRequestDto;
    private Room room;
    private Hotel hotel;

    @BeforeEach
    void setUp() {
        createRoomRequestDto = new CreateHotelRoomRequestDto(101, "Deluxe", "A deluxe room", 2, 150.0, new HashSet<>(Collections.singletonList("image1.jpg")));
        updateRoomRequestDto = new UpdateHotelRoomRequestDto(101, "Deluxe", "A deluxe room", 2, 150.0, RoomStatus.AVAILABLE, new HashSet<>(Collections.singletonList("image1.jpg")));

        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hotel California");

        room = new Room();
        room.setId(1L);
        room.setNumber(101);
        room.setType("Deluxe");
        room.setDescription("A deluxe room");
        room.setCapacity(2);
        room.setPricePerNight(150.0);
        room.setStatus(RoomStatus.AVAILABLE);
        room.setImageUrls(new HashSet<>(Collections.singletonList("image1.jpg")));
        room.setHotel(hotel);
    }

    @Test
    void testCreateRoom() {
        when(roomRepository.existsByNumber(anyInt())).thenReturn(false);
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.of(hotel));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        RoomResponseDto response = roomService.createRoom(createRoomRequestDto, 1L);

        assertNotNull(response);
        assertEquals(101, response.getNumber());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void testCreateRoom_ShouldThrowException_WhenRoomNumberExists() {
        when(roomRepository.existsByNumber(anyInt())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> roomService.createRoom(createRoomRequestDto, 1L));
    }

    @Test
    void testGetRoomById() {
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));

        RoomResponseDto response = roomService.getRoomById(1L);

        assertNotNull(response);
        assertEquals(101, response.getNumber());
    }

    @Test
    void testGetRoomById_ShouldThrowException_WhenRoomNotFound() {
        when(roomRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roomService.getRoomById(1L));
    }

    @Test
    void testGetRoomByReservationId() {
        when(roomRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(room));

        RoomResponseDto response = roomService.getRoomByReservationId(1L);

        assertNotNull(response);
        assertEquals(101, response.getNumber());
    }

    @Test
    void testGetRoomByReservationId_ShouldThrowException_WhenRoomNotFound() {
        when(roomRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> roomService.getRoomByReservationId(1L));
    }

    @Test
    void testGetRoomByHotelIdAndRoomNumber() {
        when(roomRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(room)));

        RoomResponseDto response = roomService.getRoomByHotelIdAndRoomNumber(1L, 101);

        assertNotNull(response);
        assertEquals(101, response.getNumber());
    }

    @Test
    void testGetRoomByHotelIdAndRoomNumber_ShouldThrowException_WhenRoomNotFound() {
        when(roomRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(Page.empty());

        assertThrows(ResourceNotFoundException.class, () -> roomService.getRoomByHotelIdAndRoomNumber(1L, 101));
    }

    @Test
    void testGetAllRooms() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Room> roomPage = new PageImpl<>(Collections.singletonList(room));
        when(roomRepository.findAll(any(Pageable.class))).thenReturn(roomPage);

        Page<RoomResponseDto> response = roomService.getAllRooms(pageable, null, null);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    void testUpdateRoom() {
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        RoomResponseDto response = roomService.updateRoom(1L, updateRoomRequestDto, true);

        assertNotNull(response);
        assertEquals(101, response.getNumber());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void testUpdateRoom_ShouldThrowException_WhenRoomNotFound() {
        when(roomRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roomService.updateRoom(1L, updateRoomRequestDto, true));
    }

    @Test
    void testUpdateRoomStatus() {
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        RoomResponseDto response = roomService.updateRoomStatus(1L, RoomStatus.OCCUPIED);

        assertNotNull(response);
        assertEquals(RoomStatus.OCCUPIED, response.getStatus());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void testUpdateRoomStatus_ShouldThrowException_WhenRoomNotFound() {
        when(roomRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roomService.updateRoomStatus(1L, RoomStatus.OCCUPIED));
    }

    @Test
    void testDeleteRoom() {
        when(roomRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(roomRepository).deleteById(anyLong());

        roomService.deleteRoom(1L);

        verify(roomRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteRoom_ShouldThrowException_WhenRoomNotFound() {
        when(roomRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> roomService.deleteRoom(1L));
    }
}