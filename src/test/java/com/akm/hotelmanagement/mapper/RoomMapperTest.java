package com.akm.hotelmanagement.mapper;

import com.akm.hotelmanagement.dto.hotel.HotelResponseDto;
import com.akm.hotelmanagement.dto.room.CreateHotelRoomRequestDto;
import com.akm.hotelmanagement.dto.room.RoomResponseDto;
import com.akm.hotelmanagement.dto.room.UpdateHotelRoomRequestDto;
import com.akm.hotelmanagement.entity.Hotel;
import com.akm.hotelmanagement.entity.Room;
import com.akm.hotelmanagement.entity.util.RoomStatus;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomMapperTest {

    @Test
    void testToCreateDto() {
        Room room = new Room();
        room.setNumber(101);
        room.setType("Deluxe");
        room.setDescription("A deluxe room");
        room.setCapacity(2);
        room.setPricePerNight(150.0);
        room.setImageUrls(new HashSet<>());

        CreateHotelRoomRequestDto dto = RoomMapper.toCreateDto(room);

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

        UpdateHotelRoomRequestDto dto = RoomMapper.toUpdateDto(room);

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

        RoomResponseDto dto = RoomMapper.toResponseDto(room);

        assertEquals(room.getId(), dto.getId());
        assertEquals(room.getNumber(), dto.getNumber());
        assertEquals(room.getType(), dto.getType());
        assertEquals(room.getDescription(), dto.getDescription());
        assertEquals(room.getCapacity(), dto.getCapacity());
        assertEquals(room.getPricePerNight(), dto.getPricePerNight());
        assertEquals(room.getStatus(), dto.getStatus());
        assertEquals(room.getImageUrls(), dto.getImageUrls());
        assertEquals(room.getHotel().getId(), dto.getHotel().getId());
        assertEquals(room.getReservations().size(), dto.getReservations().size());
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

        Room room = RoomMapper.toEntity(dto);

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

        Room updatedRoom = RoomMapper.toEntity(dto, room);

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
        RoomResponseDto dto = new RoomResponseDto(
                1L,
                101,
                "Deluxe",
                "A deluxe room",
                2,
                150.0,
                RoomStatus.AVAILABLE,
                new HashSet<>(),
                new HotelResponseDto(
                        1L,
                        "Hotel",
                        "City",
                        "Country",
                        "Address",
                        "Phone",
                        "Email",
                        4.0,
                        new HashSet<>(),
                        new HashSet<>(),
                        new HashSet<>()
                ),
                new HashSet<>()
        );

        Room room = RoomMapper.toEntity(dto);

        assertEquals(dto.getId(), room.getId());
        assertEquals(dto.getNumber(), room.getNumber());
        assertEquals(dto.getType(), room.getType());
        assertEquals(dto.getDescription(), room.getDescription());
        assertEquals(dto.getCapacity(), room.getCapacity());
        assertEquals(dto.getPricePerNight(), room.getPricePerNight());
        assertEquals(dto.getStatus(), room.getStatus());
        assertEquals(dto.getImageUrls(), room.getImageUrls());
        assertEquals(dto.getHotel().getId(), room.getHotel().getId());
        assertEquals(dto.getReservations().size(), room.getReservations().size());
    }
}