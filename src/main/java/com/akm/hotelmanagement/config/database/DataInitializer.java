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
//        userRepository.deleteAll();
//        amenityRepository.deleteAll();
//        hotelRepository.deleteAll();
//        roomRepository.deleteAll();
//        reservationRepository.deleteAll();

        return args -> {
            // Create Users
            User admin = new User();
            admin.setName("Admin");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("toor"));
            admin.setRole(UserRole.ADMIN); // Assuming 1 is for ADMIN
            admin.setEmail("admin@example.com");
            admin.setPhone("1234567890");
            userRepository.save(admin);

            User hotelAdmin = new User();
            hotelAdmin.setName("Hotel Admin");
            hotelAdmin.setUsername("hotelAdmin");
            hotelAdmin.setPassword(passwordEncoder.encode("toor"));
            hotelAdmin.setRole(UserRole.HOTEL_ADMIN); // Assuming 2 is for HOTEL_ADMIN
            hotelAdmin.setEmail("hoteladmin@example.com");
            hotelAdmin.setPhone("0987654321");
            userRepository.save(hotelAdmin);

            User user = new User();
            user.setName("User");
            user.setUsername("manthan");
            user.setPassword(passwordEncoder.encode("toor"));
            user.setRole(UserRole.USER); // Assuming 3 is for USER
            user.setEmail("user@example.com");
            user.setPhone("1122334455");
            userRepository.save(user);

            // Create Amenities
            Amenity wifi = new Amenity();
            wifi.setName("WiFi");
            wifi.setDescription("High-speed internet");
            amenityRepository.save(wifi);

            Amenity pool = new Amenity();
            pool.setName("Swimming Pool");
            pool.setDescription("A pool for swimming");
            amenityRepository.save(pool);

            Amenity gym = new Amenity();
            gym.setName("Gym");
            gym.setDescription("Gym for fitness freaks");
            amenityRepository.save(gym);


            // Create Hotels
            Hotel hotel1 = new Hotel();
            hotel1.setName("Hotel Sunshine");
            hotel1.setAddress("123 Sunshine St, Sunnyville");
            hotel1.setCity("Sunnyville");
            hotel1.setState("Sunnyland");
            hotel1.setZip("123311");
            hotel1.setRating(4.5);
            hotel1.setDescription("A hotel that shines like the sun.");
            hotel1.setImageUrls(new HashSet<>(List.of(
                    "http://example.com/hotel1/image1.jpg",
                    "http://example.com/hotel1/image2.jpg"
            )));
            hotel1.setAmenities(new HashSet<>(List.of(
                    wifi,
                    gym
            )));
            hotelRepository.save(hotel1);

            Hotel hotel2 = new Hotel();
            hotel2.setName("Hotel Moonlight");
            hotel2.setAddress("456 Moonlight Ave, Moonville");
            hotel2.setCity("Moonville");
            hotel2.setState("Moonland");
            hotel2.setZip("456622");
            hotel2.setRating(4.2);
            hotel2.setDescription("A hotel that shines like the moon.");
            hotel2.setImageUrls(new HashSet<>(List.of(
                    "http://example.com/hotel2/image1.jpg",
                    "http://example.com/hotel2/image2.jpg"
            )));
            hotel2.setAmenities(new HashSet<>(List.of(
                    wifi,
                    pool
            )));
            hotelRepository.save(hotel2);

            // Create Rooms
            Room room1 = new Room();
            room1.setHotel(hotel1);
            room1.setNumber(101);
            room1.setType("Deluxe");
            room1.setPricePerNight(100.0);
            room1.setCapacity(4);
            room1.setStatus(RoomStatus.AVAILABLE);
            room1.setDescription("A room that is deluxe");
            room1.setImageUrls(new HashSet<>(List.of(
                    "http://example.com/room1/image1.jpg",
                    "http://example.com/room1/image2.jpg"
            )));
            room1.setHotel(hotel1);
            roomRepository.save(room1);

            Room room2 = new Room();
            room2.setHotel(hotel1);
            room2.setNumber(102);
            room2.setType("Standard");
            room2.setPricePerNight(80.0);
            room2.setCapacity(2);
            room2.setStatus(RoomStatus.AVAILABLE);
            room2.setDescription("A room that is standard");
            room2.setImageUrls(new HashSet<>(List.of(
                    "http://example.com/room2/image1.jpg",
                    "http://example.com/room2/image2.jpg"
            )));
            room2.setHotel(hotel1);
            roomRepository.save(room2);

            Room room3 = new Room();
            room3.setHotel(hotel2);
            room3.setNumber(201);
            room3.setType("Suite");
            room3.setPricePerNight(150.0);
            room3.setCapacity(6);
            room3.setStatus(RoomStatus.AVAILABLE);
            room3.setDescription("A room that is a suite");
            room3.setImageUrls(new HashSet<>(List.of(
                    "http://example.com/room3/image1.jpg",
                    "http://example.com/room3/image2.jpg"
            )));
            room3.setHotel(hotel2);
            roomRepository.save(room3);

            // Create Reservations
            Reservation reservation1 = new Reservation();
            reservation1.setUser(user);
            reservation1.setRoom(room1);
            reservation1.setCheckIn(LocalDate.of(2025, 1, 1));
            reservation1.setCheckOut(LocalDate.of(2025, 1, 5));
            reservation1.setReservationDate(LocalDate.now());
            reservation1.setNumberOfGuests(2);
            reservation1.setTotalPrice(400.0);
            reservation1.setStatus(ReservationStatus.PENDING);
            reservationRepository.save(reservation1);

            Reservation reservation2 = new Reservation();
            reservation2.setUser(user);
            reservation2.setRoom(room3);
            reservation2.setCheckIn(LocalDate.of(2025, 2, 1));
            reservation2.setCheckOut(LocalDate.of(2025, 2, 3));
            reservation2.setReservationDate(LocalDate.now());
            reservation2.setTotalPrice(300);
            reservation2.setStatus(ReservationStatus.CONFIRMED);
            reservationRepository.save(reservation2);
        };
    }

}
