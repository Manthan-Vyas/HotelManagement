package com.akm.hotelmanagement.service;

import com.akm.hotelmanagement.dto.hotel.CreateHotelRequestDto;
import com.akm.hotelmanagement.dto.hotel.HotelResponseDto;
import com.akm.hotelmanagement.dto.hotel.UpdateHotelRequestDto;
import com.akm.hotelmanagement.entity.Hotel;
import com.akm.hotelmanagement.exception.ResourceAlreadyExistsException;
import com.akm.hotelmanagement.exception.ResourceNotFoundException;
import com.akm.hotelmanagement.filter.HotelSpecifications;
import com.akm.hotelmanagement.mapper.HotelMapper;
import com.akm.hotelmanagement.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Transactional
    public HotelResponseDto createHotel(CreateHotelRequestDto hotelCreateDto) {
        if (hotelRepository.existsByName(hotelCreateDto.getName())) {
            throw new ResourceAlreadyExistsException("Hotel already exists with name: " + hotelCreateDto.getName());
        }
        Hotel hotel = hotelMapper.toEntity(hotelCreateDto);
        return hotelMapper.toResponseDto(hotelRepository.save(hotel));
    }

    @Transactional(readOnly = true)
    public HotelResponseDto getHotelById(Long id) {
        return hotelRepository.findById(id)
                .map(hotelMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public HotelResponseDto getHotelByRoomId(Long roomId) {
        return hotelMapper.toResponseDto(hotelRepository.findAll(where(HotelSpecifications.hasFilter("room-id", roomId.toString()))).stream().findFirst().orElseThrow(
                () -> new ResourceNotFoundException("Hotel not found with room id: " + roomId)
        ));
    }

    @Transactional(readOnly = true)
    public List<HotelResponseDto> getHotelsByAdminUsername(String username) {
        return hotelRepository.findAllByAdminUsername(username).stream().map(hotelMapper::toResponseDto).collect(Collectors.toList());
    }

    @Transactional
    public HotelResponseDto getHotelByReservationId(Long reservationId) {
        return hotelMapper.toResponseDto(hotelRepository.findAll(where(HotelSpecifications.hasFilter("reservation-id", reservationId.toString()))).stream().findFirst().orElseThrow(
                () -> new ResourceNotFoundException("Hotel not found with reservation id: " + reservationId)
        ));

    }

    @Transactional(readOnly = true)
    public Page<HotelResponseDto> getAllHotels(Pageable pageable, String filterBy, String filterValue) {
        if (filterBy == null || filterValue == null) {
            return hotelRepository.findAll(pageable)
                    .map(hotelMapper::toResponseDto);
        }
        return hotelRepository.findAll(
                where(
                        HotelSpecifications.hasFilter(
                                filterBy,
                                filterValue
                        )
                ),
                pageable
                )
                .map(hotelMapper::toResponseDto);
    }

    @Transactional
    public HotelResponseDto updateHotel(Long id, UpdateHotelRequestDto dto, boolean isPut) {
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

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

        if (isPut) {
            if (areAllFieldsEqual(dto, hotel)) {
                throw new ResourceAlreadyExistsException("All fields are same as the existing one");
            }
        } else {
            checkForDuplicateFields(dto, hotel);
        }

        updateHotelFields(dto, hotel);
        return hotelMapper.toResponseDto(hotelRepository.save(hotel));
    }

    @Transactional
    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hotel not found with Id " + id);
        }
        hotelRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<HotelResponseDto> getHotelsByAmenityId(Long amenityId, Pageable pageable, String filterBy, String filterValue) {
        if (filterBy == null || filterValue == null) {
            return hotelRepository.findAll(
                    where(
                            HotelSpecifications.hasFilter("amenity-id", amenityId.toString())
                    ),
                    pageable
            ).map(hotelMapper::toResponseDto);
        }
        return hotelRepository.findAll(
                where(
                        HotelSpecifications.hasFilter("amenity-id", amenityId.toString())
                                .and(HotelSpecifications.hasFilter(filterBy, filterValue))
                ),
                pageable
        ).map(hotelMapper::toResponseDto);
    }

    private void checkForDuplicateFields(UpdateHotelRequestDto dto, Hotel hotel) {
        if (dto.getName() != null && dto.getName().equals(hotel.getName())) {
            System.out.println(dto.getName() + " " + hotel.getName());
            throw new ResourceAlreadyExistsException("Name is same as the existing one " + dto.getName()); // todo: how to i18n this? cause it requires another property like name dto.getName()
        }
        if (dto.getAddress() != null && dto.getAddress().equals(hotel.getAddress())) {
            throw new ResourceAlreadyExistsException("Address is same as the existing one " + dto.getAddress());
        }
        if (dto.getCity() != null && dto.getCity().equals(hotel.getCity())) {
            throw new ResourceAlreadyExistsException("City is same as the existing one " + dto.getCity());
        }
        if (dto.getState() != null && dto.getState().equals(hotel.getState())) {
            throw new ResourceAlreadyExistsException("State is same as the existing one " + dto.getState());
        }
        if (dto.getZip() != null && dto.getZip().equals(hotel.getZip())) {
            throw new ResourceAlreadyExistsException("Zip is same as the existing one " + dto.getZip());
        }
        if (dto.getDescription() != null && dto.getDescription().equals(hotel.getDescription())) {
            throw new ResourceAlreadyExistsException("Description is same as the existing one " + dto.getDescription());
        }
        if (dto.getRating() != null && dto.getRating().equals(hotel.getRating())) {
            throw new ResourceAlreadyExistsException("Rating is same as the existing one " + dto.getRating());
        }
        if (dto.getImageUrls() != null && dto.getImageUrls().equals(hotel.getImageUrls())) {
            throw new ResourceAlreadyExistsException("ImageUrls is same as the existing one " + dto.getImageUrls());
        }
    }

    private boolean areAllFieldsEqual(UpdateHotelRequestDto dto, Hotel hotel) {
        return Objects.equals(dto.getName(), hotel.getName()) &&
                Objects.equals(dto.getAddress(), hotel.getAddress()) &&
                Objects.equals(dto.getCity(), hotel.getCity()) &&
                Objects.equals(dto.getState(), hotel.getState()) &&
                Objects.equals(dto.getZip(), hotel.getZip()) &&
                Objects.equals(dto.getDescription(), hotel.getDescription()) &&
                Objects.equals(dto.getRating(), hotel.getRating()) &&
                Objects.equals(dto.getImageUrls(), hotel.getImageUrls());
    }

    private void updateHotelFields(UpdateHotelRequestDto dto, Hotel hotel) {
        if (dto.getName() != null) {
            hotel.setName(dto.getName());
        }
        if (dto.getAddress() != null) {
            hotel.setAddress(dto.getAddress());
        }
        if (dto.getCity() != null) {
            hotel.setCity(dto.getCity());
        }
        if (dto.getState() != null) {
            hotel.setState(dto.getState());
        }
        if (dto.getZip() != null) {
            hotel.setZip(dto.getZip());
        }
        if (dto.getDescription() != null) {
            hotel.setDescription(dto.getDescription());
        }
        if (dto.getRating() != null) {
            hotel.setRating(dto.getRating());
        }
        if (dto.getImageUrls() != null) {
            hotel.setImageUrls(dto.getImageUrls());
        }
    }
}