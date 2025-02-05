package com.akm.hotelmanagement.filter;

import com.akm.hotelmanagement.entity.User;
import com.akm.hotelmanagement.entity.util.UserRole;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
    public static Specification<User> hasFilter(String filterBy, String filterValue) {
        return (root, query, criteriaBuilder) -> {
            if (filterBy == null || filterValue == null) {
                return criteriaBuilder.conjunction();
            }
            return switch (filterBy.toLowerCase()) {
                case "username" -> criteriaBuilder.like(root.get("username"), "%" + filterValue + "%");
                case "email" -> criteriaBuilder.like(root.get("email"), "%" + filterValue + "%");
                case "role" -> criteriaBuilder.equal(root.get("role"), UserRole.valueOf(filterValue));
                case "enabled" -> criteriaBuilder.equal(root.get("enabled"), Boolean.parseBoolean(filterValue));
                case "reservation-id" -> criteriaBuilder.equal(root.join("reservations").get("id"), Long.parseLong(filterValue));
                default -> throw new IllegalArgumentException("Invalid filterBy value: " + filterBy);
            };
        };
    }
}