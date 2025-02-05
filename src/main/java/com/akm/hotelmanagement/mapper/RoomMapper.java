package com.akm.hotelmanagement.mapper;

import com.akm.hotelmanagement.dto.room.CreateHotelRoomRequestDto;
import com.akm.hotelmanagement.dto.room.RoomResponseDto;
import com.akm.hotelmanagement.dto.room.UpdateHotelRoomRequestDto;
import com.akm.hotelmanagement.entity.Reservation;
import com.akm.hotelmanagement.entity.Room;
import com.akm.hotelmanagement.repository.HotelRepository;
import com.akm.hotelmanagement.repository.ReservationRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoomMapper {
    private final HotelRepository hotelRepository;
    private final ReservationRepository reservationRepository;

    public CreateHotelRoomRequestDto toCreateDto(@NotNull Room room) {
        return new CreateHotelRoomRequestDto(
                room.getNumber(),
                room.getType(),
                room.getDescription(),
                room.getCapacity(),
                room.getPricePerNight(),
                room.getImageUrls()
        );
    }

    public UpdateHotelRoomRequestDto toUpdateDto(@NotNull Room room) {
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

    public RoomResponseDto toResponseDto(@NotNull Room room) {
        return new RoomResponseDto(
                room.getId(),
                room.getNumber(),
                room.getType(),
                room.getDescription(),
                room.getCapacity(),
                room.getPricePerNight(),
                room.getStatus(),
                room.getImageUrls(),
                room.getHotel().getId(),
                room.getReservations().stream().map(Reservation::getId).collect(Collectors.toSet())
        );
    }

    public Room toEntity(@NotNull CreateHotelRoomRequestDto roomDTO) {
        Room room = new Room();
        room.setNumber(roomDTO.getNumber());
        room.setType(roomDTO.getType());
        room.setDescription(roomDTO.getDescription());
        room.setCapacity(roomDTO.getCapacity());
        room.setPricePerNight(roomDTO.getPricePerNight());
        room.setImageUrls(roomDTO.getImageUrls());
        return room;
    }

    public Room toEntity(@NotNull UpdateHotelRoomRequestDto roomDTO, @Nullable Room room) {
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

    public Room toEntity(@NotNull RoomResponseDto roomDTO) {
        Room room = new Room();
        room.setId(roomDTO.getId());
        room.setNumber(roomDTO.getNumber());
        room.setType(roomDTO.getType());
        room.setDescription(roomDTO.getDescription());
        room.setCapacity(roomDTO.getCapacity());
        room.setPricePerNight(roomDTO.getPricePerNight());
        room.setStatus(roomDTO.getStatus());
        room.setImageUrls(roomDTO.getImageUrls());
        room.setHotel(hotelRepository.findById(roomDTO.getHotelId()).orElseThrow(
                () -> new IllegalArgumentException("Hotel with id " + roomDTO.getHotelId() + " not found")
        ));
        Set<Reservation> reservations = roomDTO.getReservationIds().stream().map(reservationRepository::findById).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());
        room.setReservations(reservations);
        return room;
    }
}