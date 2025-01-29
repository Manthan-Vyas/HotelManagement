package com.akm.hotelmanagement.mapper;

import com.akm.hotelmanagement.dto.hotel.HotelResponseDto;
import com.akm.hotelmanagement.dto.reservation.CreateOrUpdateUserRoomReservationRequestDto;
import com.akm.hotelmanagement.dto.reservation.ReservationResponseDto;
import com.akm.hotelmanagement.dto.room.RoomResponseDto;
import com.akm.hotelmanagement.dto.user.UserResponseDto;
import com.akm.hotelmanagement.entity.Reservation;
import com.akm.hotelmanagement.entity.Room;
import com.akm.hotelmanagement.entity.util.ReservationStatus;
import com.akm.hotelmanagement.entity.util.RoomStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.UUID;

import static com.akm.hotelmanagement.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReservationMapperTest {

    @Test
    void testToCreateDto() {
        Reservation reservation = new Reservation();
        reservation.setCheckIn(LocalDate.of(2023, 1, 1));
        reservation.setCheckOut(LocalDate.of(2023, 1, 10));
        reservation.setNumberOfGuests(2);

        CreateOrUpdateUserRoomReservationRequestDto dto = ReservationMapper.toCreateDto(reservation);

        assertEquals(reservation.getCheckIn(), dto.getCheckIn());
        assertEquals(reservation.getCheckOut(), dto.getCheckOut());
        assertEquals(reservation.getNumberOfGuests(), dto.getNumberOfGuests());
    }

    @Test
    void testToUpdateDto() {
        Reservation reservation = new Reservation();
        reservation.setCheckIn(LocalDate.of(2023, 1, 1));
        reservation.setCheckOut(LocalDate.of(2023, 1, 10));
        reservation.setNumberOfGuests(2);

        CreateOrUpdateUserRoomReservationRequestDto dto = ReservationMapper.toUpdateDto(reservation);

        assertEquals(reservation.getCheckIn(), dto.getCheckIn());
        assertEquals(reservation.getCheckOut(), dto.getCheckOut());
        assertEquals(reservation.getNumberOfGuests(), dto.getNumberOfGuests());
    }

    @Test
    void testToResponseDto() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setCheckIn(LocalDate.of(2023, 1, 1));
        reservation.setCheckOut(LocalDate.of(2023, 1, 10));
        reservation.setNumberOfGuests(2);
        reservation.setTotalPrice(1000.0);
        reservation.setReservationDate(LocalDate.of(2022, 12, 1));
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setUser(validUser());
        Room room = validRoom();
        room.setHotel(validHotel());
        reservation.setRoom(room);

        ReservationResponseDto dto = ReservationMapper.toResponseDto(reservation);

        assertEquals(reservation.getId(), dto.getId());
        assertEquals(reservation.getCheckIn(), dto.getCheckIn());
        assertEquals(reservation.getCheckOut(), dto.getCheckOut());
        assertEquals(reservation.getNumberOfGuests(), dto.getNumberOfGuests());
        assertEquals(reservation.getTotalPrice(), dto.getTotalPrice());
        assertEquals(reservation.getReservationDate(), dto.getReservationDate());
        assertEquals(reservation.getStatus(), dto.getStatus());
        assertEquals(reservation.getUser().getId(), dto.getUser().getId());
        assertEquals(reservation.getRoom().getId(), dto.getRoom().getId());
    }

    @Test
    void testToEntityFromCreateDto() {
        CreateOrUpdateUserRoomReservationRequestDto dto = new CreateOrUpdateUserRoomReservationRequestDto(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 10),
                2
        );

        Reservation reservation = ReservationMapper.toEntity(dto, null);

        assertEquals(dto.getCheckIn(), reservation.getCheckIn());
        assertEquals(dto.getCheckOut(), reservation.getCheckOut());
        assertEquals(dto.getNumberOfGuests(), reservation.getNumberOfGuests());
    }

    @Test
    void testToEntityFromResponseDto() {
        ReservationResponseDto dto = new ReservationResponseDto(
                1L,
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 10),
                2,
                1000.0,
                LocalDate.of(2022, 12, 1),
                ReservationStatus.CONFIRMED,
                new UserResponseDto(
                        UUID.randomUUID(),
                        "name",
                        "email@id.test",
                        "username",
                        "1212309310",
                        new HashSet<>()
                ),
                new RoomResponseDto(
                        1L,
                        102,
                        "delux",
                        "delux room",
                        2,
                        200,
                        RoomStatus.AVAILABLE,
                        new HashSet<>(),
                        new HotelResponseDto(
                                1L,
                                "hotel",
                                "address",
                                "city",
                                "state",
                                "123546",
                                "dslkja dsakj",
                                4.3,
                                new HashSet<>(),
                                new HashSet<>(),
                                new HashSet<>()
                        ),
                        new HashSet<>()
                )
        );

        Reservation reservation = ReservationMapper.toEntity(dto);

        assertEquals(dto.getId(), reservation.getId());
        assertEquals(dto.getCheckIn(), reservation.getCheckIn());
        assertEquals(dto.getCheckOut(), reservation.getCheckOut());
        assertEquals(dto.getNumberOfGuests(), reservation.getNumberOfGuests());
        assertEquals(dto.getTotalPrice(), reservation.getTotalPrice());
        assertEquals(dto.getReservationDate(), reservation.getReservationDate());
        assertEquals(dto.getStatus(), reservation.getStatus());
        assertEquals(dto.getUser().getId(), reservation.getUser().getId());
        assertEquals(dto.getRoom().getId(), reservation.getRoom().getId());
    }
}