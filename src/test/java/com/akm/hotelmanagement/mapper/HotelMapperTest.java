package com.akm.hotelmanagement.mapper;

import com.akm.hotelmanagement.dto.hotel.CreateHotelRequestDto;
import com.akm.hotelmanagement.dto.hotel.HotelResponseDto;
import com.akm.hotelmanagement.dto.hotel.UpdateHotelRequestDto;
import com.akm.hotelmanagement.entity.Hotel;
import com.akm.hotelmanagement.repository.AmenityRepository;
import com.akm.hotelmanagement.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HotelMapperTest {

    private HotelMapper hotelMapper;

    @BeforeEach
    void setUp() {
        RoomRepository roomRepository = Mockito.mock(RoomRepository.class);
        AmenityRepository amenityRepository = Mockito.mock(AmenityRepository.class);
        hotelMapper = new HotelMapper(roomRepository, amenityRepository);
    }

    @Test
    void testToCreateDto() {
        Hotel hotel = new Hotel();
        hotel.setName("Hotel California");
        hotel.setAddress("42 Sunset Blvd");
        hotel.setCity("Los Angeles");
        hotel.setState("CA");
        hotel.setZip("90001");
        hotel.setDescription("A lovely place");
        hotel.setRating(5);
        hotel.setImageUrls(new HashSet<>());

        CreateHotelRequestDto dto = hotelMapper.toCreateDto(hotel);

        assertEquals(hotel.getName(), dto.getName());
        assertEquals(hotel.getAddress(), dto.getAddress());
        assertEquals(hotel.getCity(), dto.getCity());
        assertEquals(hotel.getState(), dto.getState());
        assertEquals(hotel.getZip(), dto.getZip());
        assertEquals(hotel.getDescription(), dto.getDescription());
        assertEquals(hotel.getRating(), dto.getRating());
        assertEquals(hotel.getImageUrls(), dto.getImageUrls());
    }

    @Test
    void testToUpdateDto() {
        Hotel hotel = new Hotel();
        hotel.setName("Hotel California");
        hotel.setAddress("42 Sunset Blvd");
        hotel.setCity("Los Angeles");
        hotel.setState("CA");
        hotel.setZip("90001");
        hotel.setDescription("A lovely place");
        hotel.setRating(5);
        hotel.setImageUrls(new HashSet<>());

        UpdateHotelRequestDto dto = hotelMapper.toUpdateDto(hotel);

        assertEquals(hotel.getName(), dto.getName());
        assertEquals(hotel.getAddress(), dto.getAddress());
        assertEquals(hotel.getCity(), dto.getCity());
        assertEquals(hotel.getState(), dto.getState());
        assertEquals(hotel.getZip(), dto.getZip());
        assertEquals(hotel.getDescription(), dto.getDescription());
        assertEquals(hotel.getRating(), dto.getRating());
        assertEquals(hotel.getImageUrls(), dto.getImageUrls());
    }

    @Test
    void testToResponseDto() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hotel California");
        hotel.setAddress("42 Sunset Blvd");
        hotel.setCity("Los Angeles");
        hotel.setState("CA");
        hotel.setZip("90001");
        hotel.setDescription("A lovely place");
        hotel.setRating(5);
        hotel.setImageUrls(new HashSet<>());
        hotel.setRooms(new HashSet<>());
        hotel.setAmenities(new HashSet<>());

        HotelResponseDto dto = hotelMapper.toResponseDto(hotel);

        assertEquals(hotel.getId(), dto.getId());
        assertEquals(hotel.getName(), dto.getName());
        assertEquals(hotel.getAddress(), dto.getAddress());
        assertEquals(hotel.getCity(), dto.getCity());
        assertEquals(hotel.getState(), dto.getState());
        assertEquals(hotel.getZip(), dto.getZip());
        assertEquals(hotel.getDescription(), dto.getDescription());
        assertEquals(hotel.getRating(), dto.getRating());
        assertEquals(hotel.getImageUrls(), dto.getImageUrls());
        assertEquals(hotel.getRooms().size(), dto.getRoomIds().size());
        assertEquals(hotel.getAmenities().size(), dto.getAmenityIds().size());
    }

    @Test
    void testToEntityFromCreateDto() {
        CreateHotelRequestDto dto = new CreateHotelRequestDto(
                "Hotel California",
                "42 Sunset Blvd",
                "Los Angeles",
                "CA",
                "90001",
                "A lovely place",
                5,
                new HashSet<>()
        );

        Hotel hotel = hotelMapper.toEntity(dto);

        assertEquals(dto.getName(), hotel.getName());
        assertEquals(dto.getAddress(), hotel.getAddress());
        assertEquals(dto.getCity(), hotel.getCity());
        assertEquals(dto.getState(), hotel.getState());
        assertEquals(dto.getZip(), hotel.getZip());
        assertEquals(dto.getDescription(), hotel.getDescription());
        assertEquals(dto.getRating(), hotel.getRating());
        assertEquals(dto.getImageUrls(), hotel.getImageUrls());
    }

    @Test
    void testToEntityFromUpdateDto() {
        UpdateHotelRequestDto dto = new UpdateHotelRequestDto(
                "Hotel California",
                "42 Sunset Blvd",
                "Los Angeles",
                "CA",
                "90001",
                "A lovely place",
                5.0,
                new HashSet<>()
        );

        Hotel hotel = new Hotel();
        hotel.setName("Old Hotel");
        hotel.setAddress("Old Address");
        hotel.setCity("Old City");
        hotel.setState("Old State");
        hotel.setZip("00000");
        hotel.setDescription("Old description");
        hotel.setRating(3);
        hotel.setImageUrls(new HashSet<>());

        Hotel updatedHotel = hotelMapper.toEntity(dto, hotel);

        assertEquals(dto.getName(), updatedHotel.getName());
        assertEquals(dto.getAddress(), updatedHotel.getAddress());
        assertEquals(dto.getCity(), updatedHotel.getCity());
        assertEquals(dto.getState(), updatedHotel.getState());
        assertEquals(dto.getZip(), updatedHotel.getZip());
        assertEquals(dto.getDescription(), updatedHotel.getDescription());
        assertEquals(dto.getRating(), updatedHotel.getRating());
        assertEquals(dto.getImageUrls(), updatedHotel.getImageUrls());
    }

    @Test
    void testToEntityFromResponseDto() {
        HotelResponseDto dto = new HotelResponseDto(
                1L,
                "Hotel California",
                "42 Sunset Blvd",
                "Los Angeles",
                "CA",
                "90001",
                "A lovely place",
                5,
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>()
        );

        Hotel hotel = hotelMapper.toEntity(dto);

        assertEquals(dto.getId(), hotel.getId());
        assertEquals(dto.getName(), hotel.getName());
        assertEquals(dto.getAddress(), hotel.getAddress());
        assertEquals(dto.getCity(), hotel.getCity());
        assertEquals(dto.getState(), hotel.getState());
        assertEquals(dto.getZip(), hotel.getZip());
        assertEquals(dto.getDescription(), hotel.getDescription());
        assertEquals(dto.getRating(), hotel.getRating());
        assertEquals(dto.getImageUrls(), hotel.getImageUrls());
        assertEquals(dto.getRoomIds().size(), hotel.getRooms().size());
        assertEquals(dto.getAmenityIds().size(), hotel.getAmenities().size());
    }
}