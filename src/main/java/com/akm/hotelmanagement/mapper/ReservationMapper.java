package com.akm.hotelmanagement.mapper;

import com.akm.hotelmanagement.dto.reservation.CreateOrUpdateUserRoomReservationRequestDto;
import com.akm.hotelmanagement.dto.reservation.ReservationResponseDto;
import com.akm.hotelmanagement.entity.Reservation;
import com.akm.hotelmanagement.repository.RoomRepository;
import com.akm.hotelmanagement.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationMapper {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public CreateOrUpdateUserRoomReservationRequestDto toCreateDto(@NotNull Reservation reservation) {
        return new CreateOrUpdateUserRoomReservationRequestDto(
                reservation.getCheckIn(),
                reservation.getCheckOut(),
                reservation.getNumberOfGuests()
        );
    }

    public CreateOrUpdateUserRoomReservationRequestDto toUpdateDto(@NotNull Reservation reservation) {
        return toCreateDto(reservation);
    }

    public ReservationResponseDto toResponseDto(@NotNull Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getCheckIn(),
                reservation.getCheckOut(),
                reservation.getNumberOfGuests(),
                reservation.getTotalPrice(),
                reservation.getReservationDate(),
                reservation.getStatus(),
                reservation.getUser().getUsername(),
                reservation.getRoom().getId()
        );
    }

    public Reservation toEntity(@NotNull CreateOrUpdateUserRoomReservationRequestDto dto, @Nullable Reservation reservation) {
        if(reservation == null) {
            reservation = new Reservation();
        }
        reservation.setCheckIn(dto.getCheckIn());
        reservation.setCheckOut(dto.getCheckOut());
        reservation.setNumberOfGuests(dto.getNumberOfGuests());
        return reservation;
    }

    public Reservation toEntity(@NotNull ReservationResponseDto reservationResponseDto) {
        Reservation reservation = new Reservation();
        reservation.setId(reservationResponseDto.getId());
        reservation.setCheckIn(reservationResponseDto.getCheckIn());
        reservation.setCheckOut(reservationResponseDto.getCheckOut());
        reservation.setNumberOfGuests(reservationResponseDto.getNumberOfGuests());
        reservation.setTotalPrice(reservationResponseDto.getTotalPrice());
        reservation.setReservationDate(reservationResponseDto.getReservationDate());
        reservation.setStatus(reservationResponseDto.getStatus());
        reservation.setUser(userRepository.findByUsername(reservationResponseDto.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("User not found with username: " + reservationResponseDto.getUsername())
        ));
        reservation.setRoom(roomRepository.findById(reservationResponseDto.getRoomId()).orElseThrow(
                () -> new IllegalArgumentException("Room not found with id: " + reservationResponseDto.getRoomId())
        ));
        return reservation;
    }
}