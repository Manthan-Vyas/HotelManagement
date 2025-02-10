package com.akm.hotelmanagement.repository;

import com.akm.hotelmanagement.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface HotelRepository extends
        JpaRepository<Hotel, Long>,
        JpaSpecificationExecutor<Hotel> {
    boolean existsByName(String name);
    List<Hotel> findAllByAdminUsername(String username);
}