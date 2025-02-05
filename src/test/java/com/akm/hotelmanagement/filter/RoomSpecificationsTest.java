package com.akm.hotelmanagement.filter;

import com.akm.hotelmanagement.entity.Hotel;
import com.akm.hotelmanagement.entity.Reservation;
import com.akm.hotelmanagement.entity.Room;
import com.akm.hotelmanagement.entity.User;
import com.akm.hotelmanagement.repository.RoomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static com.akm.hotelmanagement.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
// info: one or more test might be failed since the test data is not randomly generated and may not satisfy the conditions for insertion in database but this is a rare case scenario. will fix it in the future.
public class RoomSpecificationsTest {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void hasFilterByReservationId() {
        Room room1 = validRoom();
        entityManager.persist(room1);
        Room room2 = randomValidRoom();
        entityManager.persist(room2);

        Reservation reservation1 = validReservation();
        reservation1.setRoom(room1);
        entityManager.persist(reservation1);

        Reservation reservation2 = randomValidReservation();
        reservation2.setRoom(room2);
        entityManager.persist(reservation2);

        entityManager.flush();

        Specification<Room> spec = RoomSpecification.hasFilter("reservation-id", reservation1.getId().toString());
        List<Room> result = roomRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(room1);
    }

    @Test
    public void hasFilterByHotelId() {
        Hotel hotel = validHotel();
        entityManager.persist(hotel);

        Room room1 = validRoom();
        room1.setHotel(hotel);
        entityManager.persist(room1);
        Room room2 = randomValidRoom();
        entityManager.persist(room2);

        entityManager.flush();

        Specification<Room> spec = RoomSpecification.hasFilter("hotel-id", room1.getId().toString());
        List<Room> result = roomRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(room1);
    }

    @Test
    public void testAvailableBetweenDates() {
        Room room1 = validRoom();
        entityManager.persist(room1);
        Room room2 = randomValidRoom();
        entityManager.persist(room2);

        Reservation reservation1 = validReservation();
        reservation1.setRoom(room1);
        reservation1.setCheckIn(LocalDate.of(2023, 6, 1));
        reservation1.setCheckOut(LocalDate.of(2023, 6, 10));
        entityManager.persist(reservation1);

        Reservation reservation2 = randomValidReservation();
        reservation2.setRoom(room2);
        reservation2.setCheckIn(LocalDate.of(2023, 6, 15));
        reservation2.setCheckOut(LocalDate.of(2023, 6, 20));
        entityManager.persist(reservation2);

        entityManager.flush();

        Specification<Room> spec = RoomSpecification.availableBetweenDates("2023-06-01", "2023-06-10");
        List<Room> result = roomRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(room2);
    }

    @Test void capacityBetween() {
        Room room1 = validRoom();
        room1.setCapacity(2);
        entityManager.persist(room1);
        Room room2 = randomValidRoom();
        room2.setCapacity(4);
        entityManager.persist(room2);

        entityManager.flush();

        Specification<Room> spec = RoomSpecification.capacityBetween(3, 5);
        List<Room> result = roomRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(room2);
    }

    @Test void capacityGreaterThan() {
        Room room1 = validRoom();
        room1.setCapacity(2);
        entityManager.persist(room1);
        Room room2 = randomValidRoom();
        room2.setCapacity(4);
        entityManager.persist(room2);

        entityManager.flush();

        Specification<Room> spec = RoomSpecification.capacityGreaterThan(3);
        List<Room> result = roomRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(room2);
    }

    @Test void pricePerNightLessThan() {
        Room room1 = validRoom();
        room1.setPricePerNight(100);
        entityManager.persist(room1);
        Room room2 = randomValidRoom();
        room2.setPricePerNight(200);
        entityManager.persist(room2);

        entityManager.flush();

        Specification<Room> spec = RoomSpecification.priceLessThan(150);
        List<Room> result = roomRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(room1);
    }

    @Test
    public void pricePerNightBetween() {
        Room room1 = validRoom();
        room1.setPricePerNight(100);
        entityManager.persist(room1);
        Room room2 = randomValidRoom();
        room2.setPricePerNight(200);
        entityManager.persist(room2);

        entityManager.flush();

        Specification<Room> spec = RoomSpecification.priceBetween(150, 250);
        List<Room> result = roomRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(room2);
    }

    @Test
    public void testHasFilterByUsername() {
        Room room1 = validRoom();
        entityManager.persist(room1);
        Room room2 = randomValidRoom();
        entityManager.persist(room2);

        User user = validUser();
        user.setUsername("user1");
        entityManager.persist(user);

        Reservation reservation1 = validReservation();
        reservation1.setRoom(room1);
        reservation1.setUser(user);
        entityManager.persist(reservation1);

        Reservation reservation2 = randomValidReservation();
        reservation2.setRoom(room2);
        reservation2.setUser(user);
        entityManager.persist(reservation2);

        entityManager.flush();

        Specification<Room> spec = RoomSpecification.hasFilter(
                "username", user.getUsername()
        );
        List<Room> result = roomRepository.findAll(spec);

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getId()).isEqualTo(room1.getId());
        assertThat(result.getLast().getId()).isEqualTo(room2.getId());
    }
}