package com.akm.hotelmanagement.filter;

import com.akm.hotelmanagement.entity.Amenity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class AmenitySpecifications {
    public static Specification<Amenity> hasFilter(String filterBy, String filterValue) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(filterBy) || !StringUtils.hasText(filterValue)) {
                return null;
            }
            return switch (filterBy.toLowerCase()) {
                case "name" -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filterValue.toLowerCase() + "%");
                case "description" -> criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + filterValue.toLowerCase() + "%");
                case "id" -> criteriaBuilder.equal(root.get("id"), filterValue);
                case "hotel-name" -> criteriaBuilder.like(root.join("hotels").get("name"), "%" + filterValue + "%");
                case "hotel-id" -> criteriaBuilder.equal(root.join("hotels").get("id"), filterValue);
                // if roomId is provided we can get the hotelId from the room and then amenities of that hotel
                case "room-id" -> criteriaBuilder.equal(root.join("hotels").join("rooms").get("id"), filterValue);

                default -> throw new IllegalArgumentException("Invalid filter field: " + filterBy);
            };
        };
    }
}