package com.akm.hotelmanagement.repository;

import com.akm.hotelmanagement.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

public interface RoomRepository extends
        JpaRepository<Room, Long>,
        JpaSpecificationExecutor<Room> {
    boolean existsByNumber(int number);
    @Query("SELECT r FROM Room r WHERE r.capacity >= :noOfGuests AND r.id NOT IN " +
            "(SELECT res.room.id FROM Reservation res WHERE " +
            "(:checkIn BETWEEN res.checkIn AND res.checkOut) OR " +
            "(:checkOut BETWEEN res.checkIn AND res.checkOut) OR " +
            "(res.checkIn BETWEEN :checkIn AND :checkOut) OR " +
            "(res.checkOut BETWEEN :checkIn AND :checkOut))")
    Page<Room> findAvailableRooms(@Param("checkIn") LocalDate checkIn,
                                  @Param("checkOut") LocalDate checkOut,
                                  @Param("noOfGuests") int noOfGuests, Pageable pageable, @Nullable Specification<Room> spec);
}