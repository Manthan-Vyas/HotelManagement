package com.akm.hotelmanagement.mapper;

import com.akm.hotelmanagement.dto.reservation.CreateOrUpdateUserRoomReservationRequestDto;
import com.akm.hotelmanagement.dto.reservation.ReservationResponseDto;
import com.akm.hotelmanagement.entity.Reservation;
import com.akm.hotelmanagement.entity.Room;
import com.akm.hotelmanagement.entity.User;
import com.akm.hotelmanagement.entity.util.ReservationStatus;
import com.akm.hotelmanagement.repository.RoomRepository;
import com.akm.hotelmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static com.akm.hotelmanagement.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ReservationMapperTest {
    private ReservationMapper reservationMapper;
    private UserRepository userRepository;
    private RoomRepository roomRepository;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        roomRepository = Mockito.mock(RoomRepository.class);
        reservationMapper = new ReservationMapper(userRepository, roomRepository);
    }

    @Test
    void testToCreateDto() {
        Reservation reservation = new Reservation();
        reservation.setCheckIn(LocalDate.of(2023, 1, 1));
        reservation.setCheckOut(LocalDate.of(2023, 1, 10));
        reservation.setNumberOfGuests(2);

        CreateOrUpdateUserRoomReservationRequestDto dto = reservationMapper.toCreateDto(reservation);

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

        CreateOrUpdateUserRoomReservationRequestDto dto = reservationMapper.toUpdateDto(reservation);

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

        ReservationResponseDto dto = reservationMapper.toResponseDto(reservation);

        assertEquals(reservation.getId(), dto.getId());
        assertEquals(reservation.getCheckIn(), dto.getCheckIn());
        assertEquals(reservation.getCheckOut(), dto.getCheckOut());
        assertEquals(reservation.getNumberOfGuests(), dto.getNumberOfGuests());
        assertEquals(reservation.getTotalPrice(), dto.getTotalPrice());
        assertEquals(reservation.getReservationDate(), dto.getReservationDate());
        assertEquals(reservation.getStatus(), dto.getStatus());
        assertEquals(reservation.getUser().getUsername(), dto.getUsername());
        assertEquals(reservation.getRoom().getId(), dto.getRoomId());
    }

    @Test
    void testToEntityFromCreateDto() {
        CreateOrUpdateUserRoomReservationRequestDto dto = new CreateOrUpdateUserRoomReservationRequestDto(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 10),
                2
        );

        Reservation reservation = reservationMapper.toEntity(dto, null);

        assertEquals(dto.getCheckIn(), reservation.getCheckIn());
        assertEquals(dto.getCheckOut(), reservation.getCheckOut());
        assertEquals(dto.getNumberOfGuests(), reservation.getNumberOfGuests());
    }

    @Test
    void testToEntityFromResponseDto() {
        User user = new User();
        user.setUsername("username");
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        Room room = new Room();
        room.setId(1L);
        when(roomRepository.save(room)).thenReturn(room);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        ReservationResponseDto dto = new ReservationResponseDto(
                1L,
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 10),
                2,
                1000.0,
                LocalDate.of(2022, 12, 1),
                ReservationStatus.CONFIRMED,
                "username",
                1L
        );

        Reservation reservation = reservationMapper.toEntity(dto);

        assertEquals(dto.getId(), reservation.getId());
        assertEquals(dto.getCheckIn(), reservation.getCheckIn());
        assertEquals(dto.getCheckOut(), reservation.getCheckOut());
        assertEquals(dto.getNumberOfGuests(), reservation.getNumberOfGuests());
        assertEquals(dto.getTotalPrice(), reservation.getTotalPrice());
        assertEquals(dto.getReservationDate(), reservation.getReservationDate());
        assertEquals(dto.getStatus(), reservation.getStatus());
        assertEquals(dto.getUsername(), reservation.getUser().getUsername());
        assertEquals(dto.getRoomId(), reservation.getRoom().getId());
    }
}