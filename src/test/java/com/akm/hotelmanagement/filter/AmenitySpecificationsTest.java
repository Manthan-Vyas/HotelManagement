package com.akm.hotelmanagement.filter;

import com.akm.hotelmanagement.entity.Amenity;
import com.akm.hotelmanagement.entity.Hotel;
import com.akm.hotelmanagement.entity.Room;
import com.akm.hotelmanagement.repository.AmenityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.List;

import static com.akm.hotelmanagement.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
// info: one or more test might be failed since the test data is not randomly generated and may not satisfy the conditions for insertion in database but this is a rare case scenario. will fix it in the future.
class AmenitySpecificationsTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AmenityRepository amenityRepository;

    @Test
    void testHasFilterByName() {
        Amenity amenity1 = validAmenity();
        amenity1.setName("Pool");
        entityManager.persist(amenity1);
        Amenity amenity2 = randomValidAmenity();
        amenity2.setName("Gym");
        entityManager.persist(amenity2);

        entityManager.flush();

        Specification<Amenity> spec = AmenitySpecifications.hasFilter("name", "Pool");
        assertThat(amenityRepository.findAll(spec)).containsExactly(amenity1);
    }

    @Test
    void testHasFilterByDescription() {
        Amenity amenity1 = validAmenity();
        amenity1.setDescription("Pool");
        entityManager.persist(amenity1);
        Amenity amenity2 = randomValidAmenity();
        amenity2.setDescription("Gym");
        entityManager.persist(amenity2);

        entityManager.flush();

        Specification<Amenity> spec = AmenitySpecifications.hasFilter("description", "Pool");
        assertThat(amenityRepository.findAll(spec)).containsExactly(amenity1);
    }

    @Test
    void testHasFilterById() {
        Amenity amenity1 = validAmenity();
        entityManager.persist(amenity1);
        Amenity amenity2 = randomValidAmenity();
        entityManager.persist(amenity2);

        entityManager.flush();

        Specification<Amenity> spec = AmenitySpecifications.hasFilter("id", amenity1.getId().toString());
        assertThat(amenityRepository.findAll(spec)).containsExactly(amenity1);
    }

    @Test
    void testHasFilterByHotelId() {
        Amenity amenity1 = validAmenity();
        entityManager.persist(amenity1);
        Amenity amenity2 = randomValidAmenity();
        entityManager.persist(amenity2);

        Hotel hotel = validHotel();
        hotel.setAmenities(new HashSet<>(List.of(amenity1)));
        entityManager.persist(hotel);

        entityManager.flush();

        Specification<Amenity> spec = AmenitySpecifications.hasFilter("hotel-id", hotel.getId().toString());
        assertThat(amenityRepository.findAll(spec)).containsExactly(amenity1);
    }

    @Test
    void testHasFilterByHotelName() {
        Amenity amenity1 = validAmenity();
        entityManager.persist(amenity1);
        Amenity amenity2 = randomValidAmenity();
        entityManager.persist(amenity2);

        Hotel hotel = validHotel();
        hotel.setAmenities(new HashSet<>(List.of(amenity1)));
        hotel.setName("ABC");
        entityManager.persist(hotel);

        entityManager.flush();

        Specification<Amenity> spec = AmenitySpecifications.hasFilter("hotel-name", "ABC");
        assertThat(amenityRepository.findAll(spec)).containsExactly(amenity1);
    }

    @Test
    void testHasFilterByRoomId() {
        Amenity amenity1 = validAmenity();
        entityManager.persist(amenity1);
        Amenity amenity2 = randomValidAmenity();
        entityManager.persist(amenity2);

        Hotel hotel = validHotel();
        hotel.setAmenities(new HashSet<>(List.of(amenity1)));
        entityManager.persist(hotel);

        Room room = validRoom();
        room.setHotel(hotel);
        entityManager.persist(room);

        entityManager.flush();

        Specification<Amenity> spec = AmenitySpecifications.hasFilter("room-id", room.getId().toString());
        assertThat(amenityRepository.findAll(spec)).containsExactly(amenity1);
    }
}