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
        // load only if empty
        if (userRepository.count() > 0 || amenityRepository.count() > 0 || hotelRepository.count() > 0 || roomRepository.count() > 0 || reservationRepository.count() > 0) {
            return args -> {
            };
        }
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
            admin.setRole(UserRole.ADMIN);
            admin.setEmail("admin@example.com");
            admin.setPhone("1234567890");
            admin.setEnabled(true);
            userRepository.save(admin);

            User root = new User();
            root.setName("Root");
            root.setUsername("root");
            root.setPassword(passwordEncoder.encode("toor"));
            root.setRole(UserRole.ADMIN);
            root.setEmail("root@example.com");
            root.setPhone("1234567891");
            root.setEnabled(true);
            userRepository.save(root);

            User hotelAdmin = new User();
            hotelAdmin.setName("Hotel Admin");
            hotelAdmin.setUsername("hotel");
            hotelAdmin.setPassword(passwordEncoder.encode("toor"));
            hotelAdmin.setRole(UserRole.HOTEL_ADMIN);
            hotelAdmin.setEmail("hoteladmin@example.com");
            hotelAdmin.setPhone("0987654321");
            hotelAdmin.setEnabled(true);
            userRepository.save(hotelAdmin);

            User hotelAdmin2 = new User();
            hotelAdmin2.setName("Hotel Admin 2");
            hotelAdmin2.setUsername("hotel2");
            hotelAdmin2.setPassword(passwordEncoder.encode("toor"));
            hotelAdmin2.setRole(UserRole.HOTEL_ADMIN);
            hotelAdmin2.setEmail("hotel2admin@example.com");
            hotelAdmin2.setPhone("0987654322");
            hotelAdmin2.setEnabled(true);
            userRepository.save(hotelAdmin2);

            User user = new User();
            user.setName("User");
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("toor"));
            user.setRole(UserRole.USER);
            user.setEmail("user@example.com");
            user.setPhone("1122334455");
            user.setEnabled(true);
            userRepository.save(user);

            User user2 = new User();
            user2.setName("User 2");
            user2.setUsername("user2");
            user2.setPassword(passwordEncoder.encode("toor"));
            user2.setRole(UserRole.USER);
            user2.setEmail("user2@example.com");
            user2.setPhone("1122334456");
            user2.setEnabled(true);
            userRepository.save(user2);

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
            Hotel hotel1 = getHotel(List.of(wifi, gym), hotelAdmin);
            hotelRepository.save(hotel1);

            Hotel hotel2 = getHotel2(List.of(wifi, pool), hotelAdmin2);
            hotelRepository.save(hotel2);

            // Create Rooms
            Room room1 = getRoom(hotel1);
            roomRepository.save(room1);

            Room room2 = getRoom2(hotel1);
            roomRepository.save(room2);

            Room room3 = getRoom3(hotel2);
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

    private static Room getRoom3(Hotel hotel2) {
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
        return room3;
    }

    private static Room getRoom2(Hotel hotel1) {
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
        return room2;
    }

    private static Room getRoom(Hotel hotel1) {
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
        return room1;
    }

    private static Hotel getHotel2(List<Amenity> amenities, User admin) {
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
        hotel2.setAmenities(new HashSet<>(amenities));
        hotel2.setAdmin(admin);
        return hotel2;
    }

    private static Hotel getHotel(List<Amenity> amenities, User admin) {
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
        hotel1.setAmenities(new HashSet<>(amenities));
        hotel1.setAdmin(admin);
        return hotel1;
    }
}