package com.akm.hotelmanagement.filter;

import com.akm.hotelmanagement.entity.Amenity;
import com.akm.hotelmanagement.entity.Hotel;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Set;

public class HotelSpecifications {
    public static Specification<Hotel> hasFilter(String filterBy, String filterValue) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(filterBy) || !StringUtils.hasText(filterValue)) {
                return null;
            }
            return switch (filterBy.toLowerCase()) {
                case "id" -> criteriaBuilder.equal(root.get("id"), filterValue);
                case "name" -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filterValue.toLowerCase() + "%");
                case "city" -> criteriaBuilder.like(criteriaBuilder.lower(root.get("city")), "%" + filterValue.toLowerCase() + "%");
                case "state" -> criteriaBuilder.like(criteriaBuilder.lower(root.get("state")), "%" + filterValue.toLowerCase() + "%");
                case "zip" -> criteriaBuilder.equal(root.get("zip"), filterValue);
                case "amenity-id" -> criteriaBuilder.equal(root.join("amenities").get("id"), Long.parseLong(filterValue));
                case "room-id" -> criteriaBuilder.equal(root.join("rooms").get("id"), Long.parseLong(filterValue));
                case "reservation-id" -> criteriaBuilder.equal(root.join("rooms").join("reservations").get("id"), Long.parseLong(filterValue));
                case "rating" -> criteriaBuilder.equal(root.get("rating"), filterValue);
                default -> throw new IllegalArgumentException("Invalid filter field: " + filterBy);
            };
        };
    }

    public static Specification<Hotel> hasAmenity(String amenity) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(amenity)) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("amenities")), "%" + amenity.toLowerCase() + "%");
        };
    } // todo: check if works

    public static Specification<Hotel> hasAmenities(Set<Long> amenities) {
        return (root, query, criteriaBuilder) -> {
            if (amenities == null || amenities.isEmpty()) {
                return null;
            }
            Join<Hotel, Amenity>  amenityJoin = root.join("amenities");
            return amenityJoin.get("name").in(amenities);
        };
    } // todo: check if works
}