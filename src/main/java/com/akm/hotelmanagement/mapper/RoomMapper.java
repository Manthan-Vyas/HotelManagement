package com.akm.hotelmanagement.mapper;

import com.akm.hotelmanagement.dto.room.CreateHotelRoomRequestDto;
import com.akm.hotelmanagement.dto.room.UpdateHotelRoomRequestDto;
import com.akm.hotelmanagement.dto.room.RoomResponseDto;
import com.akm.hotelmanagement.entity.Room;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

import java.util.stream.Collectors;

public class RoomMapper {

    public static CreateHotelRoomRequestDto toCreateDto(@NotNull Room room) {
        return new CreateHotelRoomRequestDto(
                room.getNumber(),
                room.getType(),
                room.getDescription(),
                room.getCapacity(),
                room.getPricePerNight(),
                room.getImageUrls()
        );
    }

    public static UpdateHotelRoomRequestDto toUpdateDto(@NotNull Room room) {
        return new UpdateHotelRoomRequestDto(
                room.getNumber(),
                room.getType(),
                room.getDescription(),
                room.getCapacity(),
                room.getPricePerNight(),
                room.getStatus(),
                room.getImageUrls()
        );
    }

    public static RoomResponseDto toResponseDto(@NotNull Room room) {
        return new RoomResponseDto(
                room.getId(),
                room.getNumber(),
                room.getType(),
                room.getDescription(),
                room.getCapacity(),
                room.getPricePerNight(),
                room.getStatus(),
                room.getImageUrls(),
                HotelMapper.toResponseDto(room.getHotel()),
                room.getReservations().stream().map(ReservationMapper::toResponseDto).collect(Collectors.toSet())
        );
    }

    public static Room toEntity(@NotNull CreateHotelRoomRequestDto roomDTO) {
        Room room = new Room();
        room.setNumber(roomDTO.getNumber());
        room.setType(roomDTO.getType());
        room.setDescription(roomDTO.getDescription());
        room.setCapacity(roomDTO.getCapacity());
        room.setPricePerNight(roomDTO.getPricePerNight());
        room.setImageUrls(roomDTO.getImageUrls());
        return room;
    }

    public static Room toEntity(@NotNull UpdateHotelRoomRequestDto roomDTO, @Nullable Room room) {
        if (room == null) {
            room = new Room();
        }
        if (roomDTO.getNumber() != null) {
            room.setNumber(roomDTO.getNumber());
        }
        if (roomDTO.getType() != null) {
            room.setType(roomDTO.getType());
        }
        if (roomDTO.getDescription() != null) {
            room.setDescription(roomDTO.getDescription());
        }
        if (roomDTO.getCapacity() != null) {
            room.setCapacity(roomDTO.getCapacity());
        }
        if (roomDTO.getPricePerNight() != null) {
            room.setPricePerNight(roomDTO.getPricePerNight());
        }
        if (roomDTO.getRoomStatus() != null) {
            room.setStatus(roomDTO.getRoomStatus());
        }
        room.setImageUrls(roomDTO.getImageUrls());
        return room;
    }

    public static Room toEntity(@NotNull RoomResponseDto roomDTO) {
        Room room = new Room();
        room.setId(roomDTO.getId());
        room.setNumber(roomDTO.getNumber());
        room.setType(roomDTO.getType());
        room.setDescription(roomDTO.getDescription());
        room.setCapacity(roomDTO.getCapacity());
        room.setPricePerNight(roomDTO.getPricePerNight());
        room.setStatus(roomDTO.getStatus());
        room.setImageUrls(roomDTO.getImageUrls());
        room.setHotel(HotelMapper.toEntity(roomDTO.getHotel()));
        room.setReservations(roomDTO.getReservations().stream().map(ReservationMapper::toEntity).collect(Collectors.toSet()));
        return room;
    }
}