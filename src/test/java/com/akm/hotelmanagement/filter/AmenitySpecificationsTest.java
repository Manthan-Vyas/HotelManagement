package com.akm.hotelmanagement.filter;

import com.akm.hotelmanagement.entity.Amenity;
import com.akm.hotelmanagement.entity.Hotel;
import com.akm.hotelmanagement.entity.Room;
import com.akm.hotelmanagement.repository.AmenityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static com.akm.hotelmanagement.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AmenitySpecificationsTest {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private AmenityRepository amenityRepository;

    @BeforeEach
    void setUp() {
        entityManager.clear();
    }

    private Stream<TestData> provideTestData() {
        return Stream.of(
                new TestData("name", "Pool", amenity -> amenity.setName("Pool")),
                new TestData("description", "Pool", amenity -> amenity.setDescription("Pool")),
                new TestData("id", null, amenity -> {
                }),
                new TestData("hotel-id", null, amenity -> {
                    Hotel hotel = validHotel();
                    hotel.setAmenities(new HashSet<>(List.of(amenity)));
                    entityManager.persist(hotel);
                }),
                new TestData("room-id", null, amenity -> {
                    Hotel hotel = validHotel();
                    hotel.setAmenities(new HashSet<>(List.of(amenity)));
                    entityManager.persist(hotel);
                    Room room = validRoom();
                    room.setHotel(hotel);
                    entityManager.persist(room);
                })
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void testHasFilter(TestData testData) {
        Amenity amenity1 = validAmenity();
        testData.setup.accept(amenity1);
        entityManager.persist(amenity1);
        entityManager.flush();
        entityManager.clear();

        String filterValue = testData.filterBy.equals("id") ? amenity1.getId().toString() : testData.filterValue;
        Specification<Amenity> spec = AmenitySpecifications.hasFilter(testData.filterBy, filterValue);
        assertThat(amenityRepository.findAll(spec)).containsExactly(amenity1);
    }

    private static class TestData {
        String filterBy;
        String filterValue;
        java.util.function.Consumer<Amenity> setup;

        TestData(String filterBy, String filterValue, java.util.function.Consumer<Amenity> setup) {
            this.filterBy = filterBy;
            this.filterValue = filterValue;
            this.setup = setup;
        }
    }
}