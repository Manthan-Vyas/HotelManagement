package com.akm.hotelmanagement.filter;

import com.akm.hotelmanagement.entity.Reservation;
import com.akm.hotelmanagement.entity.Room;
import com.akm.hotelmanagement.entity.util.RoomStatus;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RoomSpecification {
    public static Specification<Room> hasFilter(String filterBy, String filterValue) {
        return (root, query, criteriaBuilder) -> {
            if (filterBy == null || filterValue == null) {
                return criteriaBuilder.conjunction();
            }
            return switch (filterBy.toLowerCase()) {
                case "type" -> criteriaBuilder.equal(root.get("type"), filterValue);
                case "number" -> criteriaBuilder.equal(root.get("number"), Integer.parseInt(filterValue));
                case "status" -> criteriaBuilder.equal(root.get("status"), RoomStatus.valueOf(filterValue));
                case "capacity" -> criteriaBuilder.equal(root.get("capacity"), Integer.parseInt(filterValue));
                case "price" -> criteriaBuilder.equal(root.get("pricePerNight"), Double.parseDouble(filterValue));
                case "hotel-id" -> criteriaBuilder.equal(root.get("hotel").get("id"), Long.parseLong(filterValue));
                case "hotel-name" -> criteriaBuilder.equal(root.get("hotel").get("name"), filterValue);
                case "reservation-id" -> criteriaBuilder.equal(root.join("reservations").get("id"), Long.parseLong(filterValue));
                case "username" -> criteriaBuilder.equal(root.join("reservations").get("user").get("username"), filterValue);
                default -> throw new IllegalArgumentException("Invalid filterBy field: " + filterBy);
            };
        };
    }

    public static Specification<Room> priceBetween(double minPrice, double maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("pricePerNight"), minPrice, maxPrice);
    }

    public static Specification<Room> capacityBetween(int minCapacity, int maxCapacity) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("capacity"), minCapacity, maxCapacity);
    }

    public static Specification<Room> availableBetweenDates(String checkIn, String checkOut) {
        return (root, query, criteriaBuilder) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate checkInDate = LocalDate.parse(checkIn, formatter);
            LocalDate checkOutDate = LocalDate.parse(checkOut, formatter);


            // Subquery to find room IDs that are reserved within the given date range
            assert query != null;
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Reservation> reservationRoot = subquery.from(Reservation.class);
            subquery.select(reservationRoot.get("room").get("id"));
            subquery.where(
                    criteriaBuilder.or(
                            criteriaBuilder.between(reservationRoot.get("checkIn"), checkInDate, checkOutDate),
                            criteriaBuilder.between(reservationRoot.get("checkOut"), checkInDate, checkOutDate),
                            criteriaBuilder.and(
                                    criteriaBuilder.lessThanOrEqualTo(reservationRoot.get("checkIn"), checkInDate),
                                    criteriaBuilder.greaterThanOrEqualTo(reservationRoot.get("checkOut"), checkOutDate)
                            )
                    )
            );

            // Main query to select rooms that are not in the subquery result
            return criteriaBuilder.not(root.get("id").in(subquery));
        };
    }

    public static Specification<Room> capacityGreaterThan(int capacity) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("capacity"), capacity);
    }

    public static Specification<Room> priceLessThan(double price) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("pricePerNight"), price);
    }

}
