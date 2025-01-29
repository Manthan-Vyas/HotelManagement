package com.akm.hotelmanagement.filter;

import com.akm.hotelmanagement.entity.Reservation;
import com.akm.hotelmanagement.entity.User;
import com.akm.hotelmanagement.entity.util.UserRole;
import com.akm.hotelmanagement.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.akm.hotelmanagement.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserSpecificationsTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testHasFilterByEmail() {
        User user1 = validUser();
        user1.setEmail("john.doe@example.com");
        entityManager.persist(user1);
        User user2 = randomValidUser();
        user2.setEmail("jane.smith@example.com");
        entityManager.persist(user2);

        entityManager.flush();

        Specification<User> spec = UserSpecifications.hasFilter("email", "john.doe@example.com");
        assertThat(userRepository.findAll(spec)).containsExactly(user1);
    }

    @Test
    void testHasFilterByUsername() {
        User user1 = validUser();
        user1.setUsername("JohnDoe123");
        entityManager.persist(user1);
        User user2 = randomValidUser();
        entityManager.persist(user2);

        entityManager.flush();

        Specification<User> spec = UserSpecifications.hasFilter("username", "JohnDoe123");
        assertThat(userRepository.findAll(spec)).containsExactly(user1);
    }

    @Test
    void testHasFilterByRole() {
        User user1 = validUser();
        user1.setRole(UserRole.ADMIN);
        entityManager.persist(user1);
        User user2 = randomValidUser();
        user2.setRole(UserRole.USER);
        entityManager.persist(user2);

        entityManager.flush();

        Specification<User> spec = UserSpecifications.hasFilter("role", UserRole.ADMIN.name());
        assertThat(userRepository.findAll(spec)).containsExactly(user1);
    }

    @Test
    void testHasFilterByEnabled() {
        User user1 = validUser();
        user1.setEnabled(true);
        entityManager.persist(user1);
        User user2 = randomValidUser();
        user2.setEnabled(false);
        entityManager.persist(user2);

        entityManager.flush();

        Specification<User> spec = UserSpecifications.hasFilter("enabled", "true");
        assertThat(userRepository.findAll(spec)).containsExactly(user1);
    }

    @Test
    void testHasFilterByReservationId() {
        User user1 = validUser();
        entityManager.persist(user1);
        User user2 = randomValidUser();
        entityManager.persist(user2);

        Reservation reservation = validReservation();
        reservation.setUser(user1);
        entityManager.persist(reservation);

        entityManager.flush();

        Specification<User> spec = UserSpecifications.hasFilter("reservation-id", "1");
        assertThat(userRepository.findAll(spec)).containsExactly(user1);
    }
}