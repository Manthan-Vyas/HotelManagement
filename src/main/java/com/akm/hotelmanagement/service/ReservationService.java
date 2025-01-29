package com.akm.hotelmanagement.service;

import com.akm.hotelmanagement.dto.reservation.CreateOrUpdateUserRoomReservationRequestDto;
import com.akm.hotelmanagement.dto.reservation.CreateOrUpdateUserRoomReservationRequestDto;
import com.akm.hotelmanagement.dto.reservation.ReservationResponseDto;
import com.akm.hotelmanagement.entity.Reservation;
import com.akm.hotelmanagement.entity.Room;
import com.akm.hotelmanagement.entity.User;
import com.akm.hotelmanagement.entity.util.ReservationStatus;
import com.akm.hotelmanagement.exception.ResourceAlreadyExistsException;
import com.akm.hotelmanagement.exception.ResourceNotFoundException;
import com.akm.hotelmanagement.filter.ReservationSpecifications;
import com.akm.hotelmanagement.mapper.ReservationMapper;
import com.akm.hotelmanagement.mapper.UserMapper;
import com.akm.hotelmanagement.repository.ReservationRepository;
import com.akm.hotelmanagement.repository.RoomRepository;
import com.akm.hotelmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public ReservationResponseDto createReservation(CreateOrUpdateUserRoomReservationRequestDto dto, String username, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        if (dto.getNumberOfGuests() > room.getCapacity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Number of guests exceeds room capacity");
        }

        Reservation reservation = new Reservation();
        reservation.setCheckIn(dto.getCheckIn());
        reservation.setCheckOut(dto.getCheckOut());
        reservation.setNumberOfGuests(dto.getNumberOfGuests());
        reservation.setTotalPrice(room.getPricePerNight()); // todo: can add other charges or fees here
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setRoom(room);
        reservation.setUser(user);
        return ReservationMapper.toResponseDto(reservationRepository.save(reservation));
    }

    public ReservationResponseDto getReservationById(Long id) {
        return reservationRepository.findById(id)
                .map(ReservationMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
    }

    public Page<ReservationResponseDto> getAllReservations(Pageable pageable, String filterBy, String filterValue) {
        if (filterBy == null || filterValue == null) {
            return reservationRepository.findAll(pageable)
                    .map(ReservationMapper::toResponseDto);
        }
        return reservationRepository.findAll(
                where(ReservationSpecifications.hasFilter(
                        filterBy, filterValue
                )),
                pageable
        ).map(ReservationMapper::toResponseDto);
    }

    public Page<ReservationResponseDto> getAllUserReservations(String username, Pageable pageable, String filterBy, String filterValue) {
        if (filterBy == null || filterValue == null) {
            return reservationRepository.findAll(pageable)
                    .map(ReservationMapper::toResponseDto);
        }
        return reservationRepository.findAll(
                where(ReservationSpecifications.hasFilter("username", username))
                .and(where(ReservationSpecifications.hasFilter(
                        filterBy, filterValue
                ))),
                pageable
        ).map(ReservationMapper::toResponseDto);
    }

    public Page<ReservationResponseDto> getAllRoomReservations (Long id, Pageable pageable, String filterBy, String filterValue) {
        if (filterBy == null || filterValue == null) {
            return reservationRepository.findAll(pageable)
                    .map(ReservationMapper::toResponseDto);
        }
        return reservationRepository.findAll(
                where(ReservationSpecifications.hasFilter("room-id", id.toString()))
                .and(where(ReservationSpecifications.hasFilter(
                        filterBy, filterValue
                ))),
                pageable
        ).map(ReservationMapper::toResponseDto);
    }

    public ReservationResponseDto updateReservation(Long id, CreateOrUpdateUserRoomReservationRequestDto dto) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        reservation.setCheckIn(dto.getCheckIn());
        reservation.setCheckOut(dto.getCheckOut());
        reservation.setNumberOfGuests(dto.getNumberOfGuests());
        reservation.setStatus(ReservationStatus.PENDING);
        return ReservationMapper.toResponseDto(reservationRepository.save(reservation));
    } // todo: make dtos for admin updates also?

    public ReservationResponseDto updateReservationStatus(Long id, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow( () ->
                new ResourceNotFoundException("Reservation not found with id: " + id)
        );
        reservation.setStatus(status);

        return ReservationMapper.toResponseDto(
                reservationRepository.save(reservation)
        );
    }

    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation not found with id " + id);
        }
        reservationRepository.deleteById(id);
    }
}