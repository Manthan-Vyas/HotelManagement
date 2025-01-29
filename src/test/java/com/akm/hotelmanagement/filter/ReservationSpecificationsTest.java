package com.akm.hotelmanagement.filter;

import com.akm.hotelmanagement.entity.Reservation;
import com.akm.hotelmanagement.entity.Room;
import com.akm.hotelmanagement.entity.User;
import com.akm.hotelmanagement.repository.ReservationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static com.akm.hotelmanagement.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ReservationSpecificationsTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void testHasFilterById() {
        Reservation reservation1 = validReservation();
        entityManager.persist(reservation1);
        Reservation reservation2 = randomValidReservation();
        entityManager.persist(reservation2);

        entityManager.flush();

        Specification<Reservation> spec = ReservationSpecifications.hasFilter("id", reservation1.getId().toString());
        assertThat(reservationRepository.findAll(spec)).containsExactly(reservation1);
    }

    @Test
    void testHasFilterByCheckInDate() {
        Reservation reservation1 = validReservation();
        reservation1.setCheckIn(LocalDate.of(2023, 1, 1));
        entityManager.persist(reservation1);
        Reservation reservation2 = randomValidReservation();
        reservation2.setCheckIn(LocalDate.of(2023, 2, 1));
        entityManager.persist(reservation2);

        entityManager.flush();

        Specification<Reservation> spec = ReservationSpecifications.hasFilter("check-in", "2023-01-01");
        assertThat(reservationRepository.findAll(spec)).containsExactly(reservation1);
    }

    @Test
    void testHasFilterByCheckOutDate() {
        Reservation reservation1 = validReservation();
        reservation1.setCheckOut(LocalDate.of(2023, 1, 10));
        entityManager.persist(reservation1);
        Reservation reservation2 = randomValidReservation();
        reservation2.setCheckOut(LocalDate.of(2023, 2, 10));
        entityManager.persist(reservation2);

        entityManager.flush();

        Specification<Reservation> spec = ReservationSpecifications.hasFilter("check-out", "2023-01-10");
        assertThat(reservationRepository.findAll(spec)).containsExactly(reservation1);
    }

    @Test
    void testHasFilterByGuestName() {
        User user = validUser();
        user.setName("John Doe");
        entityManager.persist(user);

        Reservation reservation1 = validReservation();
        reservation1.setUser(user);
        entityManager.persist(reservation1);
        Reservation reservation2 = randomValidReservation();
        entityManager.persist(reservation2);

        entityManager.flush();

        Specification<Reservation> spec = ReservationSpecifications.hasFilter("user-name", "John Doe");
        assertThat(reservationRepository.findAll(spec)).containsExactly(reservation1);
    }

    @Test
    void testHasFilterByGuestId() {
        User user = validUser();
        entityManager.persist(user);

        Reservation reservation1 = validReservation();
        reservation1.setUser(user);
        entityManager.persist(reservation1);
        Reservation reservation2 = randomValidReservation();
        entityManager.persist(reservation2);

        entityManager.flush();

        Specification<Reservation> spec = ReservationSpecifications.hasFilter("user-id", user.getId().toString());
        assertThat(reservationRepository.findAll(spec)).containsExactly(reservation1);
    }

    @Test
    void testHasFilterByRoomId() {
        Room room = validRoom();
        entityManager.persist(room);

        Reservation reservation1 = validReservation();
        reservation1.setRoom(room);
        entityManager.persist(reservation1);
        Reservation reservation2 = randomValidReservation();
        entityManager.persist(reservation2);

        entityManager.flush();

        Specification<Reservation> spec = ReservationSpecifications.hasFilter("room-id", room.getId().toString());
        assertThat(reservationRepository.findAll(spec)).containsExactly(reservation1);
    }

    @Test
    void testReservationDateBetween() {
        Reservation reservation1 = validReservation();
        reservation1.setReservationDate(LocalDate.now().plusDays(1));
        entityManager.persist(reservation1);
        Reservation reservation2 = randomValidReservation();
        reservation2.setReservationDate(LocalDate.now().plusDays(20));
        entityManager.persist(reservation2);

        entityManager.flush();

        Specification<Reservation> spec = ReservationSpecifications.reservationDateBetween(LocalDate.now(), LocalDate.now().plusDays(10));
        List<Reservation> reservations = reservationRepository.findAll(spec);
        assertThat(reservations).containsExactly(reservation1);
    }
}
