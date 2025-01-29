package com.akm.hotelmanagement.util;

import com.akm.hotelmanagement.entity.*;
import com.akm.hotelmanagement.entity.util.ReservationStatus;
import com.akm.hotelmanagement.entity.util.RoomStatus;
import com.akm.hotelmanagement.entity.util.UserRole;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TestUtils {
    private static final Random RANDOM = new Random();

    public static Long randomValidId() {
        return (long) (RANDOM.nextInt(1000) + 1);
    }

    public static User validUser() {
        User user = new User();
//        user.setId(UUID.randomUUID());
        user.setName("test");
        user.setUsername("test");
        user.setEmail("test@test.test");
        user.setPassword("Test@123");
        user.setPhone("1234567890");
        user.setRole(UserRole.USER);
        user.setEnabled(true);
        return user;
    }

    public static User randomValidUser() {
        User user = new User();
//        user.setId(UUID.randomUUID());
        user.setName(TestStringUtils.generateRandomName());
        user.setUsername(TestStringUtils.generateRandomUsername());
        user.setEmail(TestStringUtils.generateRandomEmail());
        user.setPassword(TestStringUtils.generateRandomPassword());
        user.setPhone(TestStringUtils.generateRandomPhoneNumber());
        user.setRole(UserRole.USER);
        user.setEnabled(true);
        return user;
    }

    public static Set<User> randomValidUsers(int count) {
        Set<User> users = new HashSet<>();
        for (int i = 0; i < count; i++) {
            users.add(randomValidUser());
        }
        return users;
    }

    public static Amenity validAmenity() {
        Amenity amenity = new Amenity();
//        amenity.setId(randomValidId());
        amenity.setName("Pool");
        amenity.setDescription("A large swimming pool");
        return amenity;
    }

    public static Amenity randomValidAmenity() {
        Amenity amenity = new Amenity();
        amenity.setName(TestStringUtils.generateRandomName());
        amenity.setDescription(TestStringUtils.generateRandomDescription());
        return amenity;
    }

    public static Set<Amenity> randomValidAmenities(int count) {
        Set<Amenity> amenities = new HashSet<>();
        for (int i = 0; i < count; i++) {
            amenities.add(randomValidAmenity());
        }
        return amenities;
    }

    public static Hotel validHotel() {
        Hotel hotel = new Hotel();
//        hotel.setId(randomValidId());
        hotel.setName("Hotel California");
        hotel.setAddress("42 Sunset Boulevard");
        hotel.setCity("Los Angeles");
        hotel.setState("California");
        hotel.setZip("900010");
        hotel.setDescription("A lovely place");
        hotel.setRating(4.5);
        hotel.setImageUrls(Set.of("http://example.com/image.jpg"));
        return hotel;
    }

    public static Hotel randomValidHotel() {
        Hotel hotel = new Hotel();
//        hotel.setId(randomValidId());
        hotel.setName(TestStringUtils.generateRandomName());
        hotel.setAddress(TestStringUtils.generateRandomAddress());
        hotel.setCity(TestStringUtils.generateRandomCity());
        hotel.setState(TestStringUtils.generateRandomState());
        hotel.setZip(TestStringUtils.generateRandomZipCode());
        hotel.setDescription(TestStringUtils.generateRandomDescription());
        hotel.setRating(RANDOM.nextDouble(0.0, 5.0));
        hotel.setImageUrls(Set.of(TestStringUtils.generateRandomImageUrl()));
        return hotel;
    }

    public static Set<Hotel> randomValidHotels(int count) {
        Set<Hotel> hotels = new HashSet<>();
        for (int i = 0; i < count; i++) {
            hotels.add(randomValidHotel());
        }
        return hotels;
    }

    public static Room validRoom() {
        Room room = new Room();
//        room.setId(randomValidId());
        room.setNumber(101);
        room.setType("Deluxe");
        room.setDescription("A spacious room with a king-sized bed");
        room.setCapacity(2);
        room.setPricePerNight(150.0);
        room.setStatus(RoomStatus.AVAILABLE);
        room.setImageUrls(Set.of("http://example.com/image.jpg"));
        return room;
    }

    public static Room randomValidRoom() {
        Room room = new Room();
//        room.setId(randomValidId());
        room.setNumber(RANDOM.nextInt(100, 1000));
        room.setType(TestStringUtils.generateRandomName());
        room.setDescription(TestStringUtils.generateRandomDescription());
        room.setCapacity(RANDOM.nextInt(1, 10));
        room.setPricePerNight(RANDOM.nextDouble(50.0, 500.0));
        room.setStatus(RoomStatus.values()[RANDOM.nextInt(RoomStatus.values().length)]);
        room.setImageUrls(Set.of(TestStringUtils.generateRandomImageUrl()));
        return room;
    }

    public static Set<Room> randomValidRooms(int count) {
        Set<Room> rooms = new HashSet<>();
        for (int i = 0; i < count; i++) {
            rooms.add(randomValidRoom());
        }
        return rooms;
    }

    public static Reservation validReservation() {
        Reservation reservation = new Reservation();
//        reservation.setId(randomValidId());
        reservation.setCheckIn(TestDateUtils.getFutureDate(2));
        reservation.setCheckOut(TestDateUtils.getFutureDate(reservation.getCheckIn(), 3));
        reservation.setNumberOfGuests(2);
        reservation.setReservationDate(TestDateUtils.getToday());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setTotalPrice(300.0);
        return reservation;
    }

    public static Reservation randomValidReservation() {
        Reservation reservation = new Reservation();
//        reservation.setId(randomValidId());
        LocalDate checkIn = TestDateUtils.getRandomFutureDate();
        reservation.setCheckIn(checkIn);
        reservation.setCheckOut(TestDateUtils.getFutureDate(checkIn, RANDOM.nextInt(1, 10)));
        reservation.setNumberOfGuests(RANDOM.nextInt(1, 10));
        reservation.setReservationDate(TestDateUtils.getToday());
        reservation.setStatus(ReservationStatus.values()[RANDOM.nextInt(ReservationStatus.values().length)]);
        return reservation;
    }

    public static Set<Reservation> randomValidReservations(int count) {
        Set<Reservation> reservations = new HashSet<>();
        for (int i = 0; i < count; i++) {
            reservations.add(randomValidReservation());
        }
        return reservations;
    }

}
