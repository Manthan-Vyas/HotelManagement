package com.akm.hotelmanagement.mapper;

import com.akm.hotelmanagement.dto.room.CreateHotelRoomRequestDto;
import com.akm.hotelmanagement.dto.room.RoomResponseDto;
import com.akm.hotelmanagement.dto.room.UpdateHotelRoomRequestDto;
import com.akm.hotelmanagement.entity.Hotel;
import com.akm.hotelmanagement.entity.Room;
import com.akm.hotelmanagement.entity.util.RoomStatus;
import com.akm.hotelmanagement.repository.HotelRepository;
import com.akm.hotelmanagement.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RoomMapperTest {

    private RoomMapper roomMapper;
    private HotelRepository hotelRepository;

    @BeforeEach
    void setUp() {
        hotelRepository = Mockito.mock(HotelRepository.class);
        roomMapper = new RoomMapper(hotelRepository, Mockito.mock(ReservationRepository.class));
    }

    @Test
    void testToCreateDto() {
        Room room = new Room();
        room.setNumber(101);
        room.setType("Deluxe");
        room.setDescription("A deluxe room");
        room.setCapacity(2);
        room.setPricePerNight(150.0);
        room.setImageUrls(new HashSet<>());

        CreateHotelRoomRequestDto dto = roomMapper.toCreateDto(room);

        assertEquals(room.getNumber(), dto.getNumber());
        assertEquals(room.getType(), dto.getType());
        assertEquals(room.getDescription(), dto.getDescription());
        assertEquals(room.getCapacity(), dto.getCapacity());
        assertEquals(room.getPricePerNight(), dto.getPricePerNight());
        assertEquals(room.getImageUrls(), dto.getImageUrls());
    }

    @Test
    void testToUpdateDto() {
        Room room = new Room();
        room.setNumber(101);
        room.setType("Deluxe");
        room.setDescription("A deluxe room");
        room.setCapacity(2);
        room.setPricePerNight(150.0);
        room.setStatus(RoomStatus.AVAILABLE);
        room.setImageUrls(new HashSet<>());

        UpdateHotelRoomRequestDto dto = roomMapper.toUpdateDto(room);

        assertEquals(room.getNumber(), dto.getNumber());
        assertEquals(room.getType(), dto.getType());
        assertEquals(room.getDescription(), dto.getDescription());
        assertEquals(room.getCapacity(), dto.getCapacity());
        assertEquals(room.getPricePerNight(), dto.getPricePerNight());
        assertEquals(room.getStatus(), dto.getRoomStatus());
        assertEquals(room.getImageUrls(), dto.getImageUrls());
    }

    @Test
    void testToResponseDto() {
        Room room = new Room();
        room.setId(1L);
        room.setNumber(101);
        room.setType("Deluxe");
        room.setDescription("A deluxe room");
        room.setCapacity(2);
        room.setPricePerNight(150.0);
        room.setStatus(RoomStatus.AVAILABLE);
        room.setImageUrls(new HashSet<>());
        room.setHotel(new Hotel());
        room.setReservations(new HashSet<>());

        RoomResponseDto dto = roomMapper.toResponseDto(room);

        assertEquals(room.getId(), dto.getId());
        assertEquals(room.getNumber(), dto.getNumber());
        assertEquals(room.getType(), dto.getType());
        assertEquals(room.getDescription(), dto.getDescription());
        assertEquals(room.getCapacity(), dto.getCapacity());
        assertEquals(room.getPricePerNight(), dto.getPricePerNight());
        assertEquals(room.getStatus(), dto.getStatus());
        assertEquals(room.getImageUrls(), dto.getImageUrls());
        assertEquals(room.getHotel().getId(), dto.getHotelId());
        assertEquals(room.getReservations().size(), dto.getReservationIds().size());
    }

    @Test
    void testToEntityFromCreateDto() {
        CreateHotelRoomRequestDto dto = new CreateHotelRoomRequestDto(
                101,
                "Deluxe",
                "A deluxe room",
                2,
                150.0,
                new HashSet<>()
        );

        Room room = roomMapper.toEntity(dto);

        assertEquals(dto.getNumber(), room.getNumber());
        assertEquals(dto.getType(), room.getType());
        assertEquals(dto.getDescription(), room.getDescription());
        assertEquals(dto.getCapacity(), room.getCapacity());
        assertEquals(dto.getPricePerNight(), room.getPricePerNight());
        assertEquals(dto.getImageUrls(), room.getImageUrls());
    }

    @Test
    void testToEntityFromUpdateDto() {
        UpdateHotelRoomRequestDto dto = new UpdateHotelRoomRequestDto(
                101,
                "Deluxe",
                "A deluxe room",
                2,
                150.0,
                RoomStatus.AVAILABLE,
                new HashSet<>()
        );

        Room room = new Room();
        room.setNumber(102);
        room.setType("Standard");
        room.setDescription("A standard room");
        room.setCapacity(1);
        room.setPricePerNight(100.0);
        room.setStatus(RoomStatus.OCCUPIED);
        room.setImageUrls(new HashSet<>());

        Room updatedRoom = roomMapper.toEntity(dto, room);

        assertEquals(dto.getNumber(), updatedRoom.getNumber());
        assertEquals(dto.getType(), updatedRoom.getType());
        assertEquals(dto.getDescription(), updatedRoom.getDescription());
        assertEquals(dto.getCapacity(), updatedRoom.getCapacity());
        assertEquals(dto.getPricePerNight(), updatedRoom.getPricePerNight());
        assertEquals(dto.getRoomStatus(), updatedRoom.getStatus());
        assertEquals(dto.getImageUrls(), updatedRoom.getImageUrls());
    }

    @Test
    void testToEntityFromResponseDto() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        RoomResponseDto dto = new RoomResponseDto(
                1L,
                101,
                "Deluxe",
                "A deluxe room",
                2,
                150.0,
                RoomStatus.AVAILABLE,
                new HashSet<>(),
                1L,
                new HashSet<>()
        );

        Room room = roomMapper.toEntity(dto);

        assertEquals(dto.getId(), room.getId());
        assertEquals(dto.getNumber(), room.getNumber());
        assertEquals(dto.getType(), room.getType());
        assertEquals(dto.getDescription(), room.getDescription());
        assertEquals(dto.getCapacity(), room.getCapacity());
        assertEquals(dto.getPricePerNight(), room.getPricePerNight());
        assertEquals(dto.getStatus(), room.getStatus());
        assertEquals(dto.getImageUrls(), room.getImageUrls());
        assertEquals(dto.getHotelId(), room.getHotel().getId());
        assertEquals(dto.getReservationIds().size(), room.getReservations().size());
    }
}