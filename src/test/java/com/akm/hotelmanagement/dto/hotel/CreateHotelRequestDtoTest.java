package com.akm.hotelmanagement.dto.hotel;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateHotelRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        } catch (Exception e) {
            Logger.getLogger(CreateHotelRequestDtoTest.class.getName()).severe(e.getMessage());
        }
    }

    @Test
    public void testValidCreateHotelRequestDto() {
        CreateHotelRequestDto dto = new CreateHotelRequestDto(
                "Hotel California",
                "42 Sunset Boulevard",
                "Los Angeles",
                "California",
                "900010",
                "A lovely place",
                4.5,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<CreateHotelRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidName() {
        CreateHotelRequestDto dto = new CreateHotelRequestDto(
                "",
                "42 Sunset Boulevard",
                "Los Angeles",
                "California",
                "900010",
                "A lovely place",
                4.5,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<CreateHotelRequestDto>> violations = validator.validate(dto);
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.hotel.name.size}")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.required.name}")));
    }

    @Test
    public void testInvalidAddress() {
        CreateHotelRequestDto dto = new CreateHotelRequestDto(
                "Hotel California",
                "",
                "Los Angeles",
                "California",
                "900010",
                "A lovely place",
                4.5,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<CreateHotelRequestDto>> violations = validator.validate(dto);
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.hotel.address.size}")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.required.address}")));
    }

    @Test
    public void testInvalidCity() {
        CreateHotelRequestDto dto = new CreateHotelRequestDto(
                "Hotel California",
                "42 Sunset Boulevard",
                "",
                "California",
                "900010",
                "A lovely place",
                4.5,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<CreateHotelRequestDto>> violations = validator.validate(dto);
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.hotel.city.size}")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.required.city}")));
    }

    @Test
    public void testInvalidState() {
        CreateHotelRequestDto dto = new CreateHotelRequestDto(
                "Hotel California",
                "42 Sunset Boulevard",
                "Los Angeles",
                "",
                "900010",
                "A lovely place",
                4.5,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<CreateHotelRequestDto>> violations = validator.validate(dto);
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.hotel.state.size}")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.required.state}")));
    }

    @Test
    public void testInvalidZip() {
        CreateHotelRequestDto dto = new CreateHotelRequestDto(
                "Hotel California",
                "42 Sunset Boulevard",
                "Los Angeles",
                "California",
                "Abc21",
                "A lovely place",
                4.5,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<CreateHotelRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("{error.invalid.zip.pattern}", violations.iterator().next().getMessageTemplate());
    }

    @Test
    public void testInvalidDescription() {
        CreateHotelRequestDto dto = new CreateHotelRequestDto(
                "Hotel California",
                "42 Sunset Boulevard",
                "Los Angeles",
                "California",
                "900010",
                "",
                4.5,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<CreateHotelRequestDto>> violations = validator.validate(dto);
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.hotel.description.size}")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.required.description}")));
    }

    @Test
    public void testInvalidRating() {
        CreateHotelRequestDto dto = new CreateHotelRequestDto(
                "Hotel California",
                "42 Sunset Boulevard",
                "Los Angeles",
                "California",
                "900010",
                "A lovely place",
                6.0,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<CreateHotelRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("{error.hotel.rating}", violations.iterator().next().getMessageTemplate());
    }
}