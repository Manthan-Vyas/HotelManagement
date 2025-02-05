package com.akm.hotelmanagement.dto.room;

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

public class CreateHotelRoomRequestDtoTest {
    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        } catch (Exception e) {
            Logger.getLogger(CreateHotelRoomRequestDtoTest.class.getName()).severe(e.getMessage());
        }
    }

    @Test
    public void testValidCreateHotelRoomRequestDto() {
        CreateHotelRoomRequestDto dto = new CreateHotelRoomRequestDto(
                101,
                "Deluxe",
                "A spacious room with a king-sized bed",
                2,
                150.0,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<CreateHotelRoomRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidRoomNumber() {
        CreateHotelRoomRequestDto dto = new CreateHotelRoomRequestDto(
                -1,
                "Deluxe",
                "A spacious room with a king-sized bed",
                2,
                150.0,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<CreateHotelRoomRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.room.number.positive}")));
    }

    @Test
    public void testInvalidRoomType() {
        CreateHotelRoomRequestDto dto = new CreateHotelRoomRequestDto(
                101,
                "",
                "A spacious room with a king-sized bed",
                2,
                150.0,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<CreateHotelRoomRequestDto>> violations = validator.validate(dto);
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.room.type.size}")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.room.type.required}")));
    }

    @Test
    public void testInvalidDescription() {
        CreateHotelRoomRequestDto dto = new CreateHotelRoomRequestDto(
                101,
                "Deluxe",
                "",
                2,
                150.0,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<CreateHotelRoomRequestDto>> violations = validator.validate(dto);
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.room.description.size}")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.required.description}")));
    }

    @Test
    public void testInvalidCapacity() {
        CreateHotelRoomRequestDto dto = new CreateHotelRoomRequestDto(
                101,
                "Deluxe",
                "A spacious room with a king-sized bed",
                -1,
                150.0,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<CreateHotelRoomRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.room.capacity.positive}")));
    }

    @Test
    public void testInvalidPricePerNight() {
        CreateHotelRoomRequestDto dto = new CreateHotelRoomRequestDto(
                101,
                "Deluxe",
                "A spacious room with a king-sized bed",
                2,
                -150.0,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<CreateHotelRoomRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.room.price.positive}")));
    }
}