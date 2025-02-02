package com.akm.hotelmanagement.service;

import com.akm.hotelmanagement.dto.amenity.AmenityResponseDto;
import com.akm.hotelmanagement.dto.amenity.CreateAmenityRequestDto;
import com.akm.hotelmanagement.dto.amenity.UpdateAmenityRequestDto;
import com.akm.hotelmanagement.entity.Amenity;
import com.akm.hotelmanagement.exception.ResourceNotFoundException;
import com.akm.hotelmanagement.filter.AmenitySpecifications;
import com.akm.hotelmanagement.mapper.AmenityMapper;
import com.akm.hotelmanagement.repository.AmenityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class AmenityService {

    private final AmenityRepository amenityRepository;

    private final AmenityMapper amenityMapper;

    @Transactional
    public AmenityResponseDto createAmenity(CreateAmenityRequestDto dto) {
        if (amenityRepository.existsByName(dto.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Amenity with name: " + dto.getName() + " already exists"
            );
        }
        return amenityMapper.toResponseDto(amenityRepository.save(amenityMapper.toEntity(dto)));
    }

    @Transactional(readOnly = true)
    public AmenityResponseDto getAmenityById(Long id) {
        return amenityMapper.toResponseDto(
                amenityRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + id)));
    }

    @Transactional(readOnly = true)
    public Page<AmenityResponseDto> getAllAmenities(Pageable pageable, String filterBy, String filterValue) {
        if (filterBy == null || filterValue == null) {
            return amenityRepository.findAll(pageable)
                    .map(amenityMapper::toResponseDto);
        }
        return amenityRepository.findAll(
                where(AmenitySpecifications.hasFilter(filterBy, filterValue)),
                pageable
        ).map(amenityMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<AmenityResponseDto> getAmenitiesByHotelId(Long hotelId, Pageable pageable) {
        return amenityRepository.findAll(
                where(
                        AmenitySpecifications.hasFilter(
                                "hotel-id",
                                hotelId.toString()
                        )
                ),
                pageable
        ).map(amenityMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<AmenityResponseDto> getAmenitiesByRoomId(Long roomId, Pageable pageable) {
        return amenityRepository.findAll(
                where(
                        AmenitySpecifications.hasFilter(
                                "room-id",
                                roomId.toString()
                        )
                ),
                pageable
        ).map(amenityMapper::toResponseDto);
    }

    @Transactional
    public AmenityResponseDto updateAmenity(Long id, UpdateAmenityRequestDto dto, boolean isPut) {
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
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + id));
        if (!isPut) {
            if (dto.getName() != null && amenity.getName().equals(dto.getName())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Name is same as the existing one"
                );
            }
            if (dto.getDescription() != null && dto.getDescription().equals(amenity.getDescription())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Description is same as the existing one"
                );
            }
            if (dto.getName() != null) {
                amenity.setName(dto.getName());
            }
            if (dto.getDescription() != null) {
                amenity.setDescription(dto.getDescription());
            }
            return amenityMapper.toResponseDto(amenityRepository.save(amenity));
        }
        amenity.setName(dto.getName());
        amenity.setDescription(dto.getDescription());
        return amenityMapper.toResponseDto(amenityRepository.save(amenity));
    }

    @Transactional
    public void deleteAmenity(Long id) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + id));
        amenityRepository.delete(amenity);
    }
}
