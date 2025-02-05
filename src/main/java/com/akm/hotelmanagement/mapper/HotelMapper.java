package com.akm.hotelmanagement.mapper;

import com.akm.hotelmanagement.dto.hotel.CreateHotelRequestDto;
import com.akm.hotelmanagement.dto.hotel.HotelResponseDto;
import com.akm.hotelmanagement.dto.hotel.UpdateHotelRequestDto;
import com.akm.hotelmanagement.entity.Amenity;
import com.akm.hotelmanagement.entity.Hotel;
import com.akm.hotelmanagement.entity.Room;
import com.akm.hotelmanagement.repository.AmenityRepository;
import com.akm.hotelmanagement.repository.RoomRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HotelMapper {
    private final RoomRepository roomRepository;
    private final AmenityRepository amenityRepository;

    public CreateHotelRequestDto toCreateDto(@NotNull Hotel hotel) {
        return new CreateHotelRequestDto(
                hotel.getName(),
                hotel.getAddress(),
                hotel.getCity(),
                hotel.getState(),
                hotel.getZip(),
                hotel.getDescription(),
                hotel.getRating(),
                hotel.getImageUrls()
        );
    }

    public UpdateHotelRequestDto toUpdateDto(@NotNull Hotel hotel) {
        return new UpdateHotelRequestDto(
                hotel.getName(),
                hotel.getAddress(),
                hotel.getCity(),
                hotel.getState(),
                hotel.getZip(),
                hotel.getDescription(),
                hotel.getRating(),
                hotel.getImageUrls()
        );
    }

    public HotelResponseDto toResponseDto(@NotNull Hotel hotel) {
        return new HotelResponseDto(
                hotel.getId(),
                hotel.getName(),
                hotel.getAddress(),
                hotel.getCity(),
                hotel.getState(),
                hotel.getZip(),
                hotel.getDescription(),
                hotel.getRating(),
                hotel.getImageUrls(),
                hotel.getRooms().stream().map(Room::getId).collect(Collectors.toSet()),
                hotel.getAmenities().stream().map(Amenity::getId).collect(Collectors.toSet())
        );
    }

    public Hotel toEntity(@NotNull CreateHotelRequestDto hotelDTO) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelDTO.getName());
        hotel.setAddress(hotelDTO.getAddress());
        hotel.setCity(hotelDTO.getCity());
        hotel.setState(hotelDTO.getState());
        hotel.setZip(hotelDTO.getZip());
        hotel.setDescription(hotelDTO.getDescription());
        hotel.setRating(hotelDTO.getRating());
        hotel.setImageUrls(hotelDTO.getImageUrls());
        return hotel;
    }

    public Hotel toEntity(@NotNull UpdateHotelRequestDto hotelDTO, @Nullable Hotel hotel) {
        if (hotel == null) {
            hotel = new Hotel();
        }
        if (hotelDTO.getName() != null) {
            hotel.setName(hotelDTO.getName());
        }
        if (hotelDTO.getAddress() != null) {
            hotel.setAddress(hotelDTO.getAddress());
        }
        if (hotelDTO.getCity() != null) {
            hotel.setCity(hotelDTO.getCity());
        }
        if (hotelDTO.getState() != null) {
            hotel.setState(hotelDTO.getState());
        }
        if (hotelDTO.getZip() != null) {
            hotel.setZip(hotelDTO.getZip());
        }
        if (hotelDTO.getDescription() != null) {
            hotel.setDescription(hotelDTO.getDescription());
        }
        if (hotelDTO.getRating() != null) {
            hotel.setRating(hotelDTO.getRating());
        }
        if (hotelDTO.getImageUrls() != null) {
            hotel.setImageUrls(hotelDTO.getImageUrls());
        }
        return hotel;
    }

    public Hotel toEntity(@NotNull HotelResponseDto hotelDTO) {
        Hotel hotel = new Hotel();
        hotel.setId(hotelDTO.getId());
        hotel.setName(hotelDTO.getName());
        hotel.setAddress(hotelDTO.getAddress());
        hotel.setCity(hotelDTO.getCity());
        hotel.setState(hotelDTO.getState());
        hotel.setZip(hotelDTO.getZip());
        hotel.setDescription(hotelDTO.getDescription());
        hotel.setRating(hotelDTO.getRating());
        hotel.setImageUrls(hotelDTO.getImageUrls());
        Set<Room> rooms = hotelDTO.getRoomIds().stream().map(roomRepository::findById).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());
        hotel.setRooms(rooms);
        Set<Amenity> amenities = hotelDTO.getAmenityIds().stream().map(amenityRepository::findById).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());
        hotel.setAmenities(amenities);
        return hotel;
    }
}