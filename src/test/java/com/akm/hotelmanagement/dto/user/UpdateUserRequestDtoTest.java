package com.akm.hotelmanagement.dto.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateUserRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        } catch (Exception e) {
            Logger.getLogger(UpdateUserRequestDtoTest.class.getName()).severe(e.getMessage());
        }
    }

    @Test
    public void testValidUpdateUserRequestDto() {
        List<UpdateUserRequestDto> dtoList = List.of(
                new UpdateUserRequestDto(
                        "John Doe",
                        "john.doe@example.com",
                        "johndoe",
                        "Password@123",
                        "1234567890"
                ),
                new UpdateUserRequestDto(
                        null,
                        null,
                        null,
                        null,
                        null
                )
        );

        List<Set<ConstraintViolation<UpdateUserRequestDto>>> violationsList = dtoList.stream()
                .map(dto -> validator.validate(dto))
                .toList();

        violationsList.forEach(violations -> assertTrue(violations.isEmpty()));
    }

    @Test
    public void testInvalidName() {
        UpdateUserRequestDto dto = new UpdateUserRequestDto(
                "     ",
                "john.doe@example.com",
                "johndoe",
                "Password@123",
                "1234567890"
        );

        Set<ConstraintViolation<UpdateUserRequestDto>> violations =
                validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("{error.nullable.blank.name}", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidEmail() {
        UpdateUserRequestDto dto = new UpdateUserRequestDto(
                "John Doe",
                "invalid-email",
                "johndoe",
                "Password@123",
                "1234567890"
        );

        Set<ConstraintViolation<UpdateUserRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("{error.invalid.email}", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidUsername() {
        UpdateUserRequestDto dto = new UpdateUserRequestDto(
                "John Doe",
                "john.doe@example.com",
                "      ",
                "Password@123",
                "1234567890"
        );

        Set<ConstraintViolation<UpdateUserRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("{error.nullable.blank.username}", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidPassword() {
        UpdateUserRequestDto dto = new UpdateUserRequestDto(
                "John Doe",
                "john.doe@example.com",
                "johndoe",
                "password",
                "1234567890"
        );

        Set<ConstraintViolation<UpdateUserRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("{error.invalid.password.pattern}", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidPhone() {
        UpdateUserRequestDto dto = new UpdateUserRequestDto(
                "John Doe",
                "john.doe@example.com",
                "johndoe",
                "Password@123",
                "12345"
        );

        Set<ConstraintViolation<UpdateUserRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("{error.invalid.phone.pattern}", violations.iterator().next().getMessage());
    }
}