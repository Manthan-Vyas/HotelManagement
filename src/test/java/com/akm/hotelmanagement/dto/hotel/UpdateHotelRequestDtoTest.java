package com.akm.hotelmanagement.dto.hotel;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateHotelRequestDtoTest {
    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        } catch (Exception e) {
            Logger.getLogger(UpdateHotelRequestDtoTest.class.getName()).severe(e.getMessage());
        }
    }

    @Test
    public void UpdateHotelRequestDto() {
        List<UpdateHotelRequestDto> dtoList = List.of(
                new UpdateHotelRequestDto(
                        "Hotel California",
                        "42 Sunset Boulevard",
                        "Los Angeles",
                        "California",
                        "900010",
                        "A lovely place",
                        4.5,
                        Collections.singleton("http://example.com/image.jpg")
                ),
                new UpdateHotelRequestDto(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                )
        );

        List<Set<ConstraintViolation<UpdateHotelRequestDto>>> violationsList = dtoList.stream()
                .map(dto -> validator.validate(dto))
                .toList();
        violationsList.forEach(violations -> assertTrue(violations.isEmpty()));
    }

    @Test
    public void testInvalidName() {
        UpdateHotelRequestDto dto = new UpdateHotelRequestDto(
                "",
                "42 Sunset Boulevard",
                "Los Angeles",
                "California",
                "900010",
                "A lovely place",
                4.5,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<UpdateHotelRequestDto>> violations = validator.validate(dto);
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.hotel.name.size}")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.nullable.blank.name}")));
    }

    @Test
    public void testInvalidAddress() {
        UpdateHotelRequestDto dto = new UpdateHotelRequestDto(
                "Hotel California",
                "",
                "Los Angeles",
                "California",
                "900010",
                "A lovely place",
                4.5,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<UpdateHotelRequestDto>> violations = validator.validate(dto);
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.hotel.address.size}")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.nullable.blank.address}")));
    }

    @Test
    public void testInvalidCity() {
        UpdateHotelRequestDto dto = new UpdateHotelRequestDto(
                "Hotel California",
                "42 Sunset Boulevard",
                "",
                "California",
                "900010",
                "A lovely place",
                4.5,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<UpdateHotelRequestDto>> violations = validator.validate(dto);
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.hotel.city.size}")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.nullable.blank.city}")));
    }

    @Test
    public void testInvalidState() {
        UpdateHotelRequestDto dto = new UpdateHotelRequestDto(
                "Hotel California",
                "42 Sunset Boulevard",
                "Los Angeles",
                "",
                "900010",
                "A lovely place",
                4.5,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<UpdateHotelRequestDto>> violations = validator.validate(dto);
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.hotel.state.size}")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.nullable.blank.state}")));
    }

    @Test
    public void testInvalidZip() {
        UpdateHotelRequestDto dto = new UpdateHotelRequestDto(
                "Hotel California",
                "42 Sunset Boulevard",
                "Los Angeles",
                "California",
                "Abc21",
                "A lovely place",
                4.5,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<UpdateHotelRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("{error.invalid.zip.pattern}", violations.iterator().next().getMessageTemplate());
    }

    @Test
    public void testInvalidDescription() {
        UpdateHotelRequestDto dto = new UpdateHotelRequestDto(
                "Hotel California",
                "42 Sunset Boulevard",
                "Los Angeles",
                "California",
                "900010",
                "",
                4.5,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<UpdateHotelRequestDto>> violations = validator.validate(dto);
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.hotel.description.size}")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.nullable.blank.description}")));
    }

    @Test
    public void testInvalidRating() {
        UpdateHotelRequestDto dto = new UpdateHotelRequestDto(
                "Hotel California",
                "42 Sunset Boulevard",
                "Los Angeles",
                "California",
                "900010",
                "A lovely place",
                6.0,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<UpdateHotelRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("{error.hotel.rating}", violations.iterator().next().getMessageTemplate());
    }
}