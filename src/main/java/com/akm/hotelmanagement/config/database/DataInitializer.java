package com.akm.hotelmanagement.config.database;

import com.akm.hotelmanagement.entity.*;
import com.akm.hotelmanagement.entity.util.ReservationStatus;
import com.akm.hotelmanagement.entity.util.RoomStatus;
import com.akm.hotelmanagement.entity.util.UserRole;
import com.akm.hotelmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final UserRepository userRepository;
    private final AmenityRepository amenityRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner loadData() {
        if (userRepository.count() > 0 || amenityRepository.count() > 0 || hotelRepository.count() > 0 || roomRepository.count() > 0 || reservationRepository.count() > 0) {
            return _ -> {};
        }

        return _ -> {
            // Create Users
            createUsers();

            // Create Amenities
            List<Amenity> amenities = createAmenities();

            // Create Hotels and Rooms
            List<Hotel> hotels = createHotels(amenities);
            createRooms(hotels);

            // Create Reservations
            createReservations();
        };
    }

    private void createUsers() {
        userRepository.saveAll(List.of(
                createUser("Admin1", "admin1", "admin1@example.com", "1234567890", UserRole.ADMIN),
                createUser("Admin2", "admin2", "admin2@example.com", "1234567891", UserRole.ADMIN),
                createUser("Hotel Admin1", "hotel1", "hotel1admin@example.com", "0987654321", UserRole.HOTEL_ADMIN),
                createUser("Hotel Admin2", "hotel2", "hotel2admin@example.com", "0987654322", UserRole.HOTEL_ADMIN),
                createUser("Hotel Admin3", "hotel3", "hotel3admin@example.com", "0987654323", UserRole.HOTEL_ADMIN),
                createUser("User1", "user1", "user1@example.com", "1122334455", UserRole.USER),
                createUser("User2", "user2", "user2@example.com", "1122334456", UserRole.USER),
                createUser("User3", "user3", "user3@example.com", "1122334457", UserRole.USER),
                createUser("User4", "user4", "user4@example.com", "1122334458", UserRole.USER),
                createUser("User5", "user5", "user5@example.com", "1122334459", UserRole.USER)
        ));
    }

    private User createUser(String name, String username, String email, String phone, UserRole role) {
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("toor"));
        user.setRole(role);
        user.setEmail(email);
        user.setPhone(phone);
        user.setEnabled(true);
        return user;
    }

    private List<Amenity> createAmenities() {
        List<Amenity> amenities = List.of(
                createAmenity("WiFi", "High-speed internet"),
                createAmenity("Swimming Pool", "A pool for swimming"),
                createAmenity("Gym", "Gym for fitness freaks"),
                createAmenity("Spa", "Relaxing spa services"),
                createAmenity("Parking", "Free parking space"),
                createAmenity("Restaurant", "In-house restaurant"),
                createAmenity("Bar", "In-house bar"),
                createAmenity("Laundry", "Laundry services"),
                createAmenity("Room Service", "24/7 room service"),
                createAmenity("Conference Room", "Conference facilities"),
                createAmenity("Pet Friendly", "Pets allowed"),
                createAmenity("Airport Shuttle", "Airport shuttle service")
        );
        amenityRepository.saveAll(amenities);
        return amenities;
    }

    private Amenity createAmenity(String name, String description) {
        Amenity amenity = new Amenity();
        amenity.setName(name);
        amenity.setDescription(description);
        return amenity;
    }

    private List<Hotel> createHotels(List<Amenity> amenities) {
        List<Hotel> hotels = List.of(
                createHotel("Hotel Sunshine", "123 Sunshine St, Sunnyville", "Sunnyville", "Sunnyland", "123311", 4.5, "A hotel that shines like the sun.", amenities, userRepository.findByUsername("hotel1").orElse(null)),
                createHotel("Hotel Moonlight", "456 Moonlight Ave, Moonville", "Moonville", "Moonland", "456622", 4.2, "A hotel that shines like the moon.", amenities, userRepository.findByUsername("hotel2").orElse(null)),
                createHotel("Hotel Starlight", "789 Starlight Blvd, Starcity", "Starcity", "Starland", "789933", 4.7, "A hotel that shines like the stars.", amenities, userRepository.findByUsername("hotel3").orElse(null)),
                createHotel("Hotel Twilight", "101 Twilight Rd, Twilightown", "Twilightown", "Twilightland", "101122", 4.3, "A hotel that shines like the twilight.", amenities, userRepository.findByUsername("hotel1").orElse(null))
        );
        hotelRepository.saveAll(hotels);
        return hotels;
    }

    private Hotel createHotel(String name, String address, String city, String state, String zip, double rating, String description, List<Amenity> amenities, User admin) {
        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setAddress(address);
        hotel.setCity(city);
        hotel.setState(state);
        hotel.setZip(zip);
        hotel.setRating(rating);
        hotel.setDescription(description);
        hotel.setImageUrls(new HashSet<>(List.of(
                "http://example.com/" + name.toLowerCase().replace(" ", "") + "/image1.jpg",
                "http://example.com/" + name.toLowerCase().replace(" ", "") + "/image2.jpg"
        )));
        hotel.setAmenities(new HashSet<>(amenities));
        hotel.setAdmin(admin);
        return hotel;
    }

    private void createRooms(List<Hotel> hotels) {
        hotels.forEach(hotel -> IntStream.range(1, 6).forEach(i -> {
            Room room = new Room();
            room.setHotel(hotel);
            room.setNumber(i * 100 + hotel.getId().intValue());
            room.setType(i % 2 == 0 ? "Standard" : "Deluxe");
            room.setPricePerNight(50.0 + i * 10);
            room.setCapacity(i % 2 == 0 ? 2 : 4);
            room.setStatus(RoomStatus.AVAILABLE);
            room.setDescription("A " + room.getType().toLowerCase() + " room");
            room.setImageUrls(new HashSet<>(List.of(
                    "http://example.com/room" + room.getNumber() + "/image1.jpg",
                    "http://example.com/room" + room.getNumber() + "/image2.jpg"
            )));
            roomRepository.save(room);
        }));
    }

    private void createReservations() {
        List<User> users = userRepository.findAll().stream().filter(user -> user.getRole() == UserRole.USER).toList();
        List<Room> rooms = roomRepository.findAll();
        IntStream.range(0, 30).forEach(i -> {
            Reservation reservation = new Reservation();
            reservation.setUser(users.get(i % users.size()));
            reservation.setRoom(rooms.get(i % rooms.size()));
            reservation.setCheckIn(LocalDate.of(2025, 1, 1).plusDays(i));
            reservation.setCheckOut(LocalDate.of(2025, 1, 5).plusDays(i));
            reservation.setReservationDate(LocalDate.now());
            reservation.setNumberOfGuests(2 + i % 3);
            reservation.setTotalPrice(100.0 + i * 10);
            reservation.setStatus(i % 2 == 0 ? ReservationStatus.PENDING : ReservationStatus.CONFIRMED);
            reservationRepository.save(reservation);
        });
    }
}