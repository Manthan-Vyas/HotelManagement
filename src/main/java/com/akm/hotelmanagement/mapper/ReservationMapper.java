package com.akm.hotelmanagement.mapper;

import com.akm.hotelmanagement.dto.reservation.CreateOrUpdateUserRoomReservationRequestDto;
import com.akm.hotelmanagement.dto.reservation.ReservationResponseDto;
import com.akm.hotelmanagement.entity.Reservation;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

public class ReservationMapper {
    public static CreateOrUpdateUserRoomReservationRequestDto toCreateDto(@NotNull Reservation reservation) {
        return new CreateOrUpdateUserRoomReservationRequestDto(
                reservation.getCheckIn(),
                reservation.getCheckOut(),
                reservation.getNumberOfGuests()
        );
    }

    public static CreateOrUpdateUserRoomReservationRequestDto toUpdateDto(@NotNull Reservation reservation) {
        return toCreateDto(reservation);
    }

    public static ReservationResponseDto toResponseDto(@NotNull Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getCheckIn(),
                reservation.getCheckOut(),
                reservation.getNumberOfGuests(),
                reservation.getTotalPrice(),
                reservation.getReservationDate(),
                reservation.getStatus(),
                UserMapper.toResponseDto(reservation.getUser()),
                RoomMapper.toResponseDto(reservation.getRoom())
        );
    }

    public static Reservation toEntity(@NotNull CreateOrUpdateUserRoomReservationRequestDto dto, @Nullable Reservation reservation) {
        if(reservation == null) {
            reservation = new Reservation();
        }
        reservation.setCheckIn(dto.getCheckIn());
        reservation.setCheckOut(dto.getCheckOut());
        reservation.setNumberOfGuests(dto.getNumberOfGuests());
        return reservation;
    }

    public static Reservation toEntity(@NotNull ReservationResponseDto reservationResponseDto) {
        Reservation reservation = new Reservation();
        reservation.setId(reservationResponseDto.getId());
        reservation.setCheckIn(reservationResponseDto.getCheckIn());
        reservation.setCheckOut(reservationResponseDto.getCheckOut());
        reservation.setNumberOfGuests(reservationResponseDto.getNumberOfGuests());
        reservation.setTotalPrice(reservationResponseDto.getTotalPrice());
        reservation.setReservationDate(reservationResponseDto.getReservationDate());
        reservation.setStatus(reservationResponseDto.getStatus());
        reservation.setUser(UserMapper.toEntity(reservationResponseDto.getUser()));
        reservation.setRoom(RoomMapper.toEntity(reservationResponseDto.getRoom()));
        return reservation;
    }
}
