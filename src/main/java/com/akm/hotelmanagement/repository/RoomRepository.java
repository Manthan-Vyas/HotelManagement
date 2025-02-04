package com.akm.hotelmanagement.repository;

import com.akm.hotelmanagement.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoomRepository extends
        JpaRepository<Room, Long>,
        JpaSpecificationExecutor<Room> {
    boolean existsByNumber(int number);
}