package com.akm.hotelmanagement.service;

import com.akm.hotelmanagement.dto.reservation.CreateOrUpdateUserRoomReservationRequestDto;
import com.akm.hotelmanagement.dto.reservation.ReservationResponseDto;
import com.akm.hotelmanagement.entity.Hotel;
import com.akm.hotelmanagement.entity.Reservation;
import com.akm.hotelmanagement.entity.Room;
import com.akm.hotelmanagement.entity.User;
import com.akm.hotelmanagement.entity.util.ReservationStatus;
import com.akm.hotelmanagement.exception.ResourceNotFoundException;
import com.akm.hotelmanagement.mapper.ReservationMapper;
import com.akm.hotelmanagement.repository.ReservationRepository;
import com.akm.hotelmanagement.repository.RoomRepository;
import com.akm.hotelmanagement.repository.UserRepository;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReservationMapper reservationMapper;
    @InjectMocks
    private ReservationService reservationService;

    private CreateOrUpdateUserRoomReservationRequestDto createReservationRequestDto;
    private CreateOrUpdateUserRoomReservationRequestDto updateReservationRequestDto;
    private Reservation reservation;
    private Room room;
    private User user;

    @BeforeEach
    void setUp() {
        createReservationRequestDto = new CreateOrUpdateUserRoomReservationRequestDto(LocalDate.now(), LocalDate.now().plusDays(1), 4);
        updateReservationRequestDto = new CreateOrUpdateUserRoomReservationRequestDto(LocalDate.now(), LocalDate.now().plusDays(1), 4);

        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hotel California");

        room = new Room();
        room.setId(1L);
        room.setNumber(101);
        room.setHotel(hotel);
        room.setCapacity(4);

        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setRoom(room);
        reservation.setUser(user);
        reservation.setCheckIn(LocalDate.now());
        reservation.setCheckOut(LocalDate.now().plusDays(1));
    }

    @Test
    void testCreateReservation() {
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toResponseDto(any(Reservation.class))).thenReturn(new ReservationResponseDto(1L, LocalDate.now(), LocalDate.now().plusDays(1), 4, 100.0, LocalDate.now(), ReservationStatus.PENDING, "username", 1L));

        ReservationResponseDto response = reservationService.createReservation(createReservationRequestDto, "testuser", 1L);

        assertNotNull(response);
        assertEquals(1L, response.getRoomId());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testCreateReservation_ShouldThrowException_WhenRoomNotFound() {
        when(roomRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.createReservation(createReservationRequestDto, "testuser", 1L));
    }

    @Test
    void testCreateReservation_ShouldThrowException_WhenUserNotFound() {
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.createReservation(createReservationRequestDto, "testuser", 1L));
    }

    @Test
    void testGetReservationById() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationMapper.toResponseDto(any(Reservation.class))).thenReturn(new ReservationResponseDto(1L, LocalDate.now(), LocalDate.now().plusDays(1), 4, 100.0, LocalDate.now(), ReservationStatus.PENDING, "username", 1L));

        ReservationResponseDto response = reservationService.getReservationById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getRoomId());
    }

    @Test
    void testGetReservationById_ShouldThrowException_WhenReservationNotFound() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.getReservationById(1L));
    }

    @Test
    void testGetAllReservations() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Reservation> reservationPage = new PageImpl<>(Collections.singletonList(reservation));
        when(reservationRepository.findAll(any(Pageable.class))).thenReturn(reservationPage);
        when(reservationMapper.toResponseDto(any(Reservation.class))).thenReturn(new ReservationResponseDto(1L, LocalDate.now(), LocalDate.now().plusDays(1), 4, 100.0, LocalDate.now(), ReservationStatus.PENDING, "username", 1L));

        Page<ReservationResponseDto> response = reservationService.getAllReservations(pageable, null, null);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    void testUpdateReservation() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toResponseDto(any(Reservation.class))).thenReturn(new ReservationResponseDto(1L, LocalDate.now(), LocalDate.now().plusDays(1), 4, 100.0, LocalDate.now(), ReservationStatus.PENDING, "username", 1L));

        ReservationResponseDto response = reservationService.updateReservation(1L, updateReservationRequestDto);

        assertNotNull(response);
        assertEquals(1L, response.getRoomId());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testUpdateReservation_ShouldThrowException_WhenReservationNotFound() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.updateReservation(1L, updateReservationRequestDto));
    }

    @Test
    void testDeleteReservation() {
        when(reservationRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(reservationRepository).deleteById(anyLong());

        reservationService.deleteReservation(1L);

        verify(reservationRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteReservation_ShouldThrowException_WhenReservationNotFound() {
        when(reservationRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> reservationService.deleteReservation(1L));
    }
}