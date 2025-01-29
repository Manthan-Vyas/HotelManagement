package com.akm.hotelmanagement.filter;

import com.akm.hotelmanagement.entity.Reservation;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.UUID;

public class ReservationSpecifications {
    public static Specification<Reservation> hasFilter(String filterBy, String filterValue) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(filterBy) || !StringUtils.hasText(filterValue)) {
                return null;
            }
            return switch (filterBy.toLowerCase()) {
                case "id" -> criteriaBuilder.equal(root.get("id"), filterValue);
                case "room-id" -> criteriaBuilder.equal(root.get("room").get("id"), filterValue);
                case "user-id" -> criteriaBuilder.equal(root.get("user").get("id"), UUID.fromString(filterValue));
                case "user-name" -> criteriaBuilder.like(criteriaBuilder.lower(root.get("user").get("name")), "%" + filterValue.toLowerCase() + "%");
                case "check-in" -> criteriaBuilder.equal(root.get("checkIn"), LocalDate.parse(filterValue));
                case "check-out" -> criteriaBuilder.equal(root.get("checkOut"), LocalDate.parse(filterValue));
                case "reservation-date" -> criteriaBuilder.equal(root.get("reservationDate"), LocalDate.parse(filterValue));
                case "total-guests" -> criteriaBuilder.equal(root.get("totalGuests"), Integer.parseInt(filterValue));
                case "status" -> criteriaBuilder.equal(root.get("status"), filterValue);
                default -> throw new IllegalArgumentException("Invalid filter field: " + filterBy);
            };
        };
    }

    public static Specification<Reservation> reservationDateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.between(root.get("reservationDate"), startDate, endDate);
        };
    }
}
