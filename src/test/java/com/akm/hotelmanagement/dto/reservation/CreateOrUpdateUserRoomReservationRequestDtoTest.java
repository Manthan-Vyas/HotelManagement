package com.akm.hotelmanagement.dto.reservation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateOrUpdateUserRoomReservationRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        } catch (Exception e) {
            Logger.getLogger(CreateOrUpdateUserRoomReservationRequestDtoTest.class.getName()).severe(e.getMessage());
        }
    }

    @Test
    public void testValidReservation() {
        CreateOrUpdateUserRoomReservationRequestDto dto = new CreateOrUpdateUserRoomReservationRequestDto(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                2
        );

        Set<ConstraintViolation<CreateOrUpdateUserRoomReservationRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidCheckInDate() {
        CreateOrUpdateUserRoomReservationRequestDto dto = new CreateOrUpdateUserRoomReservationRequestDto(
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(2),
                2
        );

        Set<ConstraintViolation<CreateOrUpdateUserRoomReservationRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.reservation.check-in.before-today}")));
    }

    @Test
    public void testInvalidCheckOutDate() {
        CreateOrUpdateUserRoomReservationRequestDto dto = new CreateOrUpdateUserRoomReservationRequestDto(
                LocalDate.now().plusDays(1),
                LocalDate.now(),
                2
        );

        Set<ConstraintViolation<CreateOrUpdateUserRoomReservationRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.reservation.check-out.before-check-in}")));
    }

    @Test
    public void testInvalidNumberOfGuests() {
        CreateOrUpdateUserRoomReservationRequestDto dto = new CreateOrUpdateUserRoomReservationRequestDto(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                0
        );

        Set<ConstraintViolation<CreateOrUpdateUserRoomReservationRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.reservation.number-of-guests}")));
    }
}