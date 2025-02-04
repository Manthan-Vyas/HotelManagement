package com.akm.hotelmanagement.service;

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
import com.akm.hotelmanagement.repository.ReservationRepository;
import com.akm.hotelmanagement.repository.RoomRepository;
import com.akm.hotelmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    private final ReservationMapper reservationMapper;

    @Transactional
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
        reservation.setReservationDate(LocalDate.now());
        reservation.setNumberOfGuests(dto.getNumberOfGuests());
        long daysOfStay = ChronoUnit.DAYS.between(dto.getCheckIn(), dto.getCheckOut());
        double totalPrice = room.getPricePerNight() * (daysOfStay + 1);
        reservation.setTotalPrice(totalPrice); // todo: can add other charges or fees here
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setRoom(room);
        reservation.setUser(user);
        return reservationMapper.toResponseDto(reservationRepository.save(reservation));
    }

    @Transactional(readOnly = true)
    public ReservationResponseDto getReservationById(Long id) {
        return reservationRepository.findById(id)
                .map(reservationMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<ReservationResponseDto> getAllReservations(Pageable pageable, String filterBy, String filterValue) {
        if (filterBy == null || filterValue == null) {
            return reservationRepository.findAll(pageable)
                    .map(reservationMapper::toResponseDto);
        }
        return reservationRepository.findAll(
                where(ReservationSpecifications.hasFilter(
                        filterBy, filterValue
                )),
                pageable
        ).map(reservationMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<ReservationResponseDto> getReservationsByUsername(String username, Pageable pageable, String filterBy, String filterValue) {
        if (filterBy == null || filterValue == null) {
            return reservationRepository.findAll(
                    where(ReservationSpecifications.hasFilter("username", username)),
                    pageable
                    ).map(reservationMapper::toResponseDto);
        }
        return reservationRepository.findAll(
                where(ReservationSpecifications.hasFilter("username", username))
                .and(where(ReservationSpecifications.hasFilter(
                        filterBy, filterValue
                ))),
                pageable
        ).map(reservationMapper::toResponseDto);
    }

    @Transactional
    public Page<ReservationResponseDto> getReservationsByHotelId(Long id, Pageable pageable, String filterBy, String filterValue) {
        if (filterBy == null || filterValue == null) {
            return reservationRepository.findAll(where(
                    ReservationSpecifications.hasFilter(
                            "hotel-id", id.toString()
                    )),
                    pageable
            ).map(reservationMapper::toResponseDto);
        }
        return reservationRepository.findAll(
                where(ReservationSpecifications.hasFilter("hotel-id", id.toString()))
                        .and(where(ReservationSpecifications.hasFilter(
                                filterBy, filterValue
                        ))),
                pageable
        ).map(reservationMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<ReservationResponseDto> getReservationsByRoomId(Long id, Pageable pageable, String filterBy, String filterValue) {
        if (filterBy == null || filterValue == null) {
            return reservationRepository.findAll(where(
                    ReservationSpecifications.hasFilter(
                            "room-id", id.toString()
                    )),
                    pageable
            ).map(reservationMapper::toResponseDto);
        }
        return reservationRepository.findAll(
                where(
                        ReservationSpecifications.hasFilter(
                                        "room-id", id.toString()
                        ).and(
                                ReservationSpecifications.hasFilter(
                                        filterBy, filterValue
                                )
                        )
                ),
                pageable
        ).map(reservationMapper::toResponseDto);
    }

    @Transactional
    public ReservationResponseDto updateReservation(Long id, CreateOrUpdateUserRoomReservationRequestDto dto) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        reservation.setCheckIn(dto.getCheckIn());
        reservation.setCheckOut(dto.getCheckOut());
        reservation.setNumberOfGuests(dto.getNumberOfGuests());
        reservation.setStatus(ReservationStatus.PENDING);
        return reservationMapper.toResponseDto(reservationRepository.save(reservation));
    } // todo: make dtos for admin updates also?

    @Transactional
    public ReservationResponseDto updateReservationStatus(Long id, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow( () ->
                new ResourceNotFoundException("Reservation not found with id: " + id)
        );
        if (reservation.getStatus() == ReservationStatus.CANCELLED_BY_USER) {
            throw new ResourceAlreadyExistsException("Reservation already cancelled by user");
        }
        reservation.setStatus(status);
        return reservationMapper.toResponseDto(
                reservationRepository.save(reservation)
        );
    }

    @Transactional
    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation not found with id " + id);
        }
        reservationRepository.deleteById(id);
    }

    @Transactional
    public ReservationResponseDto cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));
        if (reservation.getStatus() == ReservationStatus.CANCELLED || reservation.getStatus() == ReservationStatus.CANCELLED_BY_USER) {
            throw new ResourceAlreadyExistsException("Reservation already cancelled");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        return reservationMapper.toResponseDto(reservationRepository.save(reservation));
    }
}