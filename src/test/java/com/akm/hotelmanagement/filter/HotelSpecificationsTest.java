package com.akm.hotelmanagement.filter;

import com.akm.hotelmanagement.entity.Hotel;
import com.akm.hotelmanagement.repository.HotelRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.akm.hotelmanagement.util.TestUtils.randomValidHotel;
import static com.akm.hotelmanagement.util.TestUtils.validHotel;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
// info: one or more test might be failed since the test data is not randomly generated and may not satisfy the conditions for insertion in database but this is a rare case scenario. will fix it in the future.
public class HotelSpecificationsTest {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private HotelRepository hotelRepository;

    @Test
    void testHasFilterById() {
        Hotel hotel1 = validHotel();
        entityManager.persist(hotel1);
        Hotel hotel2 = randomValidHotel();
        entityManager.persist(hotel2);

        entityManager.flush();

        Specification<Hotel> spec = HotelSpecifications.hasFilter("id", hotel1.getId().toString());
        assertThat(hotelRepository.findAll(spec)).containsExactly(hotel1);
    }

    @Test
    void testHasFilterByName() {
        Hotel hotel1 = validHotel();
        hotel1.setName("Hotel ABC");
        entityManager.persist(hotel1);
        Hotel hotel2 = randomValidHotel();
        hotel2.setName("Hotel XYZ");
        entityManager.persist(hotel2);

        entityManager.flush();

        Specification<Hotel> spec = HotelSpecifications.hasFilter("name", "ABC");
        assertThat(hotelRepository.findAll(spec)).containsExactly(hotel1);
    }

    @Test
    void testHasFilterByCity() {
        Hotel hotel1 = validHotel();
        hotel1.setCity("New York");
        entityManager.persist(hotel1);
        Hotel hotel2 = randomValidHotel();
        hotel2.setCity("Los Angeles");
        entityManager.persist(hotel2);

        entityManager.flush();

        Specification<Hotel> spec = HotelSpecifications.hasFilter("city", "New York");
        assertThat(hotelRepository.findAll(spec)).containsExactly(hotel1);
    }

    @Test
    void testHasFilterByState() {
        Hotel hotel1 = validHotel();
        hotel1.setState("NY");
        entityManager.persist(hotel1);
        Hotel hotel2 = randomValidHotel();
        hotel2.setState("CA");
        entityManager.persist(hotel2);

        entityManager.flush();

        Specification<Hotel> spec = HotelSpecifications.hasFilter("state", "NY");
        assertThat(hotelRepository.findAll(spec)).containsExactly(hotel1);
    }

    @Test
    void testHasFilterByZip() {
        Hotel hotel1 = validHotel();
        hotel1.setZip("10001");
        entityManager.persist(hotel1);
        Hotel hotel2 = randomValidHotel();
        hotel2.setZip("90001");
        entityManager.persist(hotel2);

        entityManager.flush();

        Specification<Hotel> spec = HotelSpecifications.hasFilter("zip", "10001");
        assertThat(hotelRepository.findAll(spec)).containsExactly(hotel1);
    }

    @Test
    void testHasFilterByRating() {
        Hotel hotel1 = validHotel();
        hotel1.setRating(5);
        entityManager.persist(hotel1);
        Hotel hotel2 = randomValidHotel();
        hotel2.setRating(3);
        entityManager.persist(hotel2);

        entityManager.flush();

        Specification<Hotel> spec = HotelSpecifications.hasFilter("rating", "5");
        assertThat(hotelRepository.findAll(spec)).containsExactly(hotel1);
    }
}