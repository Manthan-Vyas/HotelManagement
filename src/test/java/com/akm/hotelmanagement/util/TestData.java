package com.akm.hotelmanagement.util;

import com.akm.hotelmanagement.entity.*;
import com.akm.hotelmanagement.entity.util.ReservationStatus;
import com.akm.hotelmanagement.entity.util.RoomStatus;
import com.akm.hotelmanagement.entity.util.UserRole;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TestData {
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "toor";
    public static final String HOTEL_ADMIN_USERNAME = "hotel";
    public static final String HOTEL_ADMIN_PASSWORD = "toor";
    public static final String USER_USERNAME = "user";
    public static final String USER_PASSWORD = "toor";

    public static final Hotel HOTEL1 = createHotel(1L, "Hotel 1", "123 Main St", "City 1", "State 1", "12345", "Description 1", 4.5, Set.of("https://example.com/image1.jpg", "https://example.com/image2.jpg"));
    public static final Hotel HOTEL2 = createHotel(2L, "Hotel 2", "456 Main St", "City 2", "State 2", "23456", "Description 2", 4.0, Set.of("https://example.com/image3.jpg", "https://example.com/image4.jpg"));
    public static final Hotel HOTEL3 = createHotel(3L, "Hotel 3", "789 Main St", "City 3", "State 3", "34567", "Description 3", 3.5, Set.of("https://example.com/image5.jpg", "https://example.com/image6.jpg"));
    public static final Hotel HOTEL4 = createHotel(4L, "Hotel 4", "101 Main St", "City 4", "State 4", "45678", "Description 4", 3.0, Set.of("https://example.com/image7.jpg", "https://example.com/image8.jpg"));

    public static final Amenity AMENITY1 = createAmenity(1L, "Free Wi-Fi", "Free Wi-Fi Description", Set.of(HOTEL1, HOTEL2));
    public static final Amenity AMENITY2 = createAmenity(2L, "Free Parking", "Free Parking Description", Set.of(HOTEL1, HOTEL3));
    public static final Amenity AMENITY3 = createAmenity(3L, "Swimming Pool", "Swimming Pool Description", Set.of(HOTEL1, HOTEL2));
    public static final Amenity AMENITY4 = createAmenity(4L, "Gym", "Gym Description", Set.of(HOTEL1, HOTEL4));
    public static final Amenity AMENITY5 = createAmenity(5L, "Spa", "Spa Description", Set.of(HOTEL4));
    public static final Amenity AMENITY6 = createAmenity(6L, "Restaurant", "Restaurant Description", Set.of(HOTEL2, HOTEL4));
    public static final Amenity AMENITY7 = createAmenity(7L, "Bar", "Bar Description", Set.of(HOTEL3));
    public static final Amenity AMENITY8 = createAmenity(8L, "Room Service", "Room Service Description", Set.of(HOTEL2, HOTEL4));
    public static final Amenity AMENITY9 = createAmenity(9L, "Laundry Service", "Laundry Service Description", Set.of(HOTEL3));
    public static final Amenity AMENITY10 = createAmenity(10L, "Airport Shuttle", "Airport Shuttle Description", Set.of(HOTEL3));
    public static final Amenity AMENITY11 = createAmenity(11L, "Meeting Room", "Meeting Room Description", Set.of(HOTEL4));
    public static final Amenity AMENITY12 = createAmenity(12L, "Conference Room", "Conference Room Description", Set.of(HOTEL4));

    public static final List<Amenity> AMENITIES = createAmenities();

    public static final List<Hotel> HOTELS = createHotels();

    public static final List<Room> ROOMS = createRooms();
    public static final List<Reservation> RESERVATIONS = createReservations();
    public static final List<User> USERS = createUsers();

    private static Hotel createHotel(Long id, String name, String address, String city, String state, String zip, String description, double rating, Set<String> imageUrls) {
        Hotel hotel = new Hotel();
        hotel.setId(id);
        hotel.setName(name);
        hotel.setAddress(address);
        hotel.setCity(city);
        hotel.setState(state);
        hotel.setZip(zip);
        hotel.setDescription(description);
        hotel.setRating(rating);
        hotel.setImageUrls(imageUrls);
        return hotel;
    }

    private static Amenity createAmenity(Long id, String name, String description, Set<Hotel> hotels) {
        Amenity amenity = new Amenity();
        amenity.setId(id);
        amenity.setName(name);
        amenity.setDescription(description);
        amenity.setHotels(hotels);
        return amenity;
    }

    private static List<Amenity> createAmenities() {
        List<Amenity> amenities = new ArrayList<>();
        amenities.add(AMENITY1);
        amenities.add(AMENITY2);
        amenities.add(AMENITY3);
        amenities.add(AMENITY4);
        amenities.add(AMENITY5);
        amenities.add(AMENITY6);
        amenities.add(AMENITY7);
        amenities.add(AMENITY8);
        amenities.add(AMENITY9);
        amenities.add(AMENITY10);
        amenities.add(AMENITY11);
        amenities.add(AMENITY12);
        return amenities;
    }

    private static List<Hotel> createHotels() {
        List<Hotel> hotels = new ArrayList<>();
        hotels.add(HOTEL1);
        hotels.add(HOTEL2);
        hotels.add(HOTEL3);
        hotels.add(HOTEL4);
        return hotels;
    }

    private static List<Room> createRooms() {
        List<Room> rooms = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Room room = new Room();
            room.setId((long) i);
            room.setNumber(100 + i);
            room.setType("Type " + i);
            room.setDescription("Description " + i);
            room.setCapacity(i % 4 + 1);
            room.setPricePerNight(100.0 + i * 10);
            room.setStatus(RoomStatus.AVAILABLE);
            room.setImageUrls(Set.of("https://example.com/room" + i + ".jpg"));
            room.setHotel(i <= 5 ? HOTEL1 : i <= 10 ? HOTEL2 : i <= 15 ? HOTEL3 : HOTEL4);
            rooms.add(room);
        }
        return rooms;
    }

    private static List<Reservation> createReservations() {
        List<Reservation> reservations = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            Reservation reservation = new Reservation();
            reservation.setId((long) i);
            reservation.setCheckIn(LocalDate.parse("2021-01-01").plusDays(i * 5));
            reservation.setCheckOut(LocalDate.parse("2021-01-05").plusDays(i * 5));
            reservation.setNumberOfGuests(i % 4 + 1);
            reservation.setTotalPrice(400.0 + i * 50);
            reservation.setReservationDate(LocalDate.parse("2020-12-01").plusDays(i));
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservation.setUser(i <= 5 ? USERS.get(3) : i <= 10 ? USERS.get(4) : USERS.get(5));
            reservation.setRoom(ROOMS.get(i - 1));
            reservations.add(reservation);
        }
        return reservations;
    }

    private static List<User> createUsers() {
        List<User> users = new ArrayList<>();
        users.add(createUser(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Admin User", "admin@user.test", "admin", "toor", "1234567890", UserRole.ADMIN, true, Set.of()));
        users.add(createUser(UUID.fromString("00000000-0000-0000-0000-000000000002"), "Hotel Admin User", "hotel@admin.test", "hotel", "toor", "2345678901", UserRole.HOTEL_ADMIN, true, Set.of()));
        users.add(createUser(UUID.fromString("00000000-0000-0000-0000-000000000003"), "Regular User 1", "user@1.test", "user", "toor", "3456789012", UserRole.USER, true, Set.of(RESERVATIONS.get(10), RESERVATIONS.get(11), RESERVATIONS.get(12), RESERVATIONS.get(13), RESERVATIONS.get(14))));
        users.add(createUser(UUID.fromString("00000000-0000-0000-0000-000000000004"), "Regular User 2", "user@2.test", "user2", "toor", "4567890123", UserRole.USER, true, Set.of(RESERVATIONS.get(0), RESERVATIONS.get(1), RESERVATIONS.get(2), RESERVATIONS.get(3), RESERVATIONS.get(4))));
        users.add(createUser(UUID.fromString("00000000-0000-0000-0000-000000000005"), "Regular User 3", "user@3.test", "user3", "toor", "5678901234", UserRole.USER, true, Set.of(RESERVATIONS.get(5), RESERVATIONS.get(6), RESERVATIONS.get(7), RESERVATIONS.get(8), RESERVATIONS.get(9))));
        users.add(createUser(UUID.fromString("00000000-0000-0000-0000-000000000006"), "Disabled User", "user@disabled.test", "disabled", "toor", "6789012345", UserRole.USER, false, Set.of(RESERVATIONS.get(15))));
        return users;
    }

    private static User createUser(UUID id, String name, String email, String username, String password, String phone, UserRole role, boolean enabled, Set<Reservation> reservations) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setPhone(phone);
        user.setRole(role);
        user.setEnabled(enabled);
        user.setReservations(reservations);
        return user;
    }
}