package com.akm.hotelmanagement.util.mapper;

import com.akm.hotelmanagement.dto.hotel.CreateHotelRequestDto;
import com.akm.hotelmanagement.dto.hotel.UpdateHotelRequestDto;
import com.akm.hotelmanagement.dto.hotel.HotelResponseDto;
import com.akm.hotelmanagement.entity.Hotel;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

import java.util.stream.Collectors;

public class HotelMapper {

    public static CreateHotelRequestDto toCreateDto(@NotNull Hotel hotel) {
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

    public static UpdateHotelRequestDto toUpdateDto(@NotNull Hotel hotel) {
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

    public static HotelResponseDto toResponseDto(@NotNull Hotel hotel) {
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
                hotel.getRooms().stream().map(RoomMapper::toResponseDto).collect(Collectors.toSet()),
                hotel.getAmenities().stream().map(AmenityMapper::toResponseDto).collect(Collectors.toSet())
        );
    }

    public static Hotel toEntity(@NotNull CreateHotelRequestDto hotelDTO) {
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

    public static Hotel toEntity(@NotNull UpdateHotelRequestDto hotelDTO, @Nullable Hotel hotel) {
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

    public static Hotel toEntity(@NotNull HotelResponseDto hotelDTO) {
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
        hotel.setRooms(hotelDTO.getRooms().stream().map(RoomMapper::toEntity).collect(Collectors.toSet()));
        hotel.setAmenities(hotelDTO.getAmenities().stream().map(AmenityMapper::toEntity).collect(Collectors.toSet()));
        return hotel;
    }
}