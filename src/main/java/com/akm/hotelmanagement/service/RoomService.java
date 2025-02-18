package com.akm.hotelmanagement.service;

import com.akm.hotelmanagement.dto.room.CreateHotelRoomRequestDto;
import com.akm.hotelmanagement.dto.room.RoomResponseDto;
import com.akm.hotelmanagement.dto.room.UpdateHotelRoomRequestDto;
import com.akm.hotelmanagement.entity.Hotel;
import com.akm.hotelmanagement.entity.Room;
import com.akm.hotelmanagement.entity.util.RoomStatus;
import com.akm.hotelmanagement.exception.ResourceAlreadyExistsException;
import com.akm.hotelmanagement.exception.ResourceNotFoundException;
import com.akm.hotelmanagement.filter.RoomSpecification;
import com.akm.hotelmanagement.mapper.RoomMapper;
import com.akm.hotelmanagement.repository.HotelRepository;
import com.akm.hotelmanagement.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Objects;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomMapper roomMapper;

    @Transactional
    public RoomResponseDto createRoom(CreateHotelRoomRequestDto roomCreateDto, Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
        Room room = roomMapper.toEntity(roomCreateDto);
        room.setStatus(RoomStatus.AVAILABLE);
        room.setHotel(hotel);
        return roomMapper.toResponseDto(roomRepository.save(room));
    }

    @Transactional(readOnly = true)
    public RoomResponseDto getRoomById(Long id) {
        return roomRepository.findById(id)
                .map(roomMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public RoomResponseDto getRoomByReservationId(Long reservationId) {
        return roomRepository.findAll(
                        where(RoomSpecification.hasFilter("reservation-id", reservationId.toString()))
                ).stream().findFirst().map(roomMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with reservation id: " + reservationId));
    }

    @Transactional(readOnly = true)
    public RoomResponseDto getRoomByHotelIdAndRoomNumber(Long hotelId, int roomNumber) {
        return roomRepository.findAll(
                        where(
                                (root, query, criteriaBuilder) -> {
                                    assert query != null;
                                    query.distinct(true);
                                    return criteriaBuilder.and(
                                            criteriaBuilder.equal(root.get("hotel").get("id"), hotelId),
                                            criteriaBuilder.equal(root.get("number"), roomNumber)
                                    );
                                }
                        ), Pageable.unpaged())
                .map(roomMapper::toResponseDto).stream().findFirst().orElse(null);
    }

    @Transactional(readOnly = true)
    public Page<RoomResponseDto> getAllRooms(Pageable pageable, String filterBy, String filterValue) {
        if (filterBy == null || filterValue == null) {
            return roomRepository.findAll(pageable)
                    .map(roomMapper::toResponseDto);
        }
        return roomRepository.findAll(
                        where(
                                RoomSpecification.hasFilter(filterBy, filterValue)
                        ),
                        pageable
                )
                .map(roomMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<RoomResponseDto> getAvailableRooms(LocalDate checkIn, LocalDate checkOut, int noOfGuests, Pageable pageable, String filterBy, String filterValue) {
        if (filterBy == null || filterValue == null) {
            return roomRepository.findAvailableRooms(checkIn, checkOut, noOfGuests, pageable, null).map(roomMapper::toResponseDto);
        }
        return roomRepository.findAvailableRooms(
                checkIn, checkOut, noOfGuests, pageable, where(RoomSpecification.hasFilter(filterBy, filterValue))
        ).map(roomMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<RoomResponseDto> getRoomsByHotelId(Long hotelId, Pageable pageable, String filterBy, String filterValue) {
        if (filterBy == null || filterValue == null) {
            return roomRepository.findAll(
                            where(RoomSpecification.hasFilter("hotel-id", hotelId.toString())),
                            pageable
                    )
                    .map(roomMapper::toResponseDto);
        }
        return roomRepository.findAll(
                where(RoomSpecification.hasFilter("hotel-id", hotelId.toString()))
                        .and(where(RoomSpecification.hasFilter(filterBy, filterValue))),
                pageable
        ).map(roomMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<RoomResponseDto> getRoomsByAmenityId(Long hotelId, Pageable pageable, String filterBy, String filterValue) {
        if (filterBy == null || filterValue == null) {
            return roomRepository.findAll(
                            where(RoomSpecification.hasFilter("amenity-id", hotelId.toString())),
                            pageable
                    )
                    .map(roomMapper::toResponseDto);
        }
        return roomRepository.findAll(
                where(RoomSpecification.hasFilter("amenity-id", hotelId.toString()))
                        .and(where(RoomSpecification.hasFilter(filterBy, filterValue))),
                pageable
        ).map(roomMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<RoomResponseDto> getRoomsByUsername(String username, Pageable pageable, String filterBy, String filterValue) {
        if (filterBy == null || filterValue == null) {
            return roomRepository.findAll(
                    where(
                            RoomSpecification.hasFilter("username", username)),
                    pageable
            ).map(roomMapper::toResponseDto);
        }
        return roomRepository.findAll(
                where(RoomSpecification.hasFilter("username", username))
                        .and(where(RoomSpecification.hasFilter(filterBy, filterValue))),
                pageable
        ).map(roomMapper::toResponseDto);
    }

    @Transactional
    public RoomResponseDto updateRoom(Long id, UpdateHotelRoomRequestDto dto, boolean isPut) {
        if (isPut && dto.hasAnyFieldNull()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "All fields are required for PUT request"
            );
        }
        if (!isPut && dto.hasAllFieldsNull()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "At least one field is required for PATCH request"
            );
        }

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with Id: " + id));

        if (isPut) {
            if (areAllFieldsEqual(dto, room)) {
                throw new ResourceAlreadyExistsException("All fields are same as the existing one");
            }
        } else {
            checkForDuplicateFields(dto, room);
        }

        updateRoomFields(dto, room);
        return roomMapper.toResponseDto(roomRepository.save(room));
    }

    @Transactional
    public RoomResponseDto updateRoomStatus(Long id, RoomStatus status) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with Id: " + id));
        room.setStatus(status);
        return roomMapper.toResponseDto(roomRepository.save(room));
    }

    @Transactional
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room not found with Id " + id);
        }
        roomRepository.deleteById(id);
    }

    private void checkForDuplicateFields(UpdateHotelRoomRequestDto dto, Room room) {
        if (dto.getNumber() != null && dto.getNumber().equals(room.getNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Room number is the same as the existing one: " + dto.getNumber()
            );
        }
        if (dto.getType() != null && dto.getType().equals(room.getType())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Room type is the same as the existing one: " + dto.getType()
            );
        }
        if (dto.getDescription() != null && dto.getDescription().equals(room.getDescription())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Room description is the same as the existing one: " + dto.getDescription()
            );
        }
        if (dto.getCapacity() != null && dto.getCapacity().equals(room.getCapacity())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Room capacity is the same as the existing one: " + dto.getCapacity()
            );
        }
        if (dto.getPricePerNight() != null && dto.getPricePerNight().equals(room.getPricePerNight())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Room price per night is the same as the existing one: " + dto.getPricePerNight()
            );
        }
        if (dto.getImageUrls() != null && dto.getImageUrls().equals(room.getImageUrls())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Room image URLs are the same as the existing ones"
            );
        }
    }

    private boolean areAllFieldsEqual(UpdateHotelRoomRequestDto dto, Room room) {
        return Objects.equals(dto.getNumber(), room.getNumber()) &&
                Objects.equals(dto.getType(), room.getType()) &&
                Objects.equals(dto.getDescription(), room.getDescription()) &&
                Objects.equals(dto.getCapacity(), room.getCapacity()) &&
                Objects.equals(dto.getPricePerNight(), room.getPricePerNight()) &&
                Objects.equals(dto.getImageUrls(), room.getImageUrls());
    }

    private void updateRoomFields(UpdateHotelRoomRequestDto dto, Room room) {
        if (dto.getNumber() != null) {
            room.setNumber(dto.getNumber());
        }
        if (dto.getType() != null) {
            room.setType(dto.getType());
        }
        if (dto.getDescription() != null) {
            room.setDescription(dto.getDescription());
        }
        if (dto.getCapacity() != null) {
            room.setCapacity(dto.getCapacity());
        }
        if (dto.getPricePerNight() != null) {
            room.setPricePerNight(dto.getPricePerNight());
        }
        if (dto.getImageUrls() != null) {
            room.setImageUrls(dto.getImageUrls());
        }
    }
}