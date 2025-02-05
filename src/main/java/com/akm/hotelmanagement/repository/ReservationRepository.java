package com.akm.hotelmanagement.repository;

import com.akm.hotelmanagement.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;

public interface ReservationRepository extends
        JpaRepository<Reservation, Long>,
        JpaSpecificationExecutor<Reservation> {
    boolean existsByRoomIdAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(Long roomId, LocalDate checkOut, LocalDate checkIn);
}