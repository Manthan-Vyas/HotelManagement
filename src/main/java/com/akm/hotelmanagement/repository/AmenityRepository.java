package com.akm.hotelmanagement.repository;

import com.akm.hotelmanagement.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AmenityRepository extends
        JpaRepository<Amenity, Long>,
        JpaSpecificationExecutor<Amenity> {
    boolean existsByName(String name);
}