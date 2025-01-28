package com.akm.hotelmanagement.dto.room;

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

public class UpdateHotelRoomRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        } catch (Exception e) {
            Logger.getLogger(UpdateHotelRoomRequestDtoTest.class.getName()).severe(e.getMessage());
        }
    }

    @Test
    public void testValidUpdateHotelRoomRequestDto() {
        List<UpdateHotelRoomRequestDto> dtoList = List.of(
                new UpdateHotelRoomRequestDto(
                        101,
                        "Deluxe",
                        "A spacious room with a king-sized bed",
                        2,
                        150.0,
                        null,
                        Collections.singleton("http://example.com/image.jpg")
                ),
                new UpdateHotelRoomRequestDto(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                )
        );

        List<Set<ConstraintViolation<UpdateHotelRoomRequestDto>>> violationsList = dtoList.stream().map(dto -> validator.validate(dto)).toList();
        violationsList.forEach(violations -> assertTrue(violations.isEmpty()));
    }

    @Test
    public void testInvalidRoomNumber() {
        UpdateHotelRoomRequestDto dto = new UpdateHotelRoomRequestDto(
                -1,
                "Deluxe",
                "A spacious room with a king-sized bed",
                2,
                150.0,
                null,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<UpdateHotelRoomRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.room.number.positive}")));
    }

    @Test
    public void testInvalidRoomType() {
        UpdateHotelRoomRequestDto dto = new UpdateHotelRoomRequestDto(
                101,
                "",
                "A spacious room with a king-sized bed",
                2,
                150.0,
                null,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<UpdateHotelRoomRequestDto>> violations = validator.validate(dto);
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.room.type.size}")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.room.type.nullable.blank}")));
    }

    @Test
    public void testInvalidDescription() {
        UpdateHotelRoomRequestDto dto = new UpdateHotelRoomRequestDto(
                101,
                "Deluxe",
                "",
                2,
                150.0,
                null,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<UpdateHotelRoomRequestDto>> violations = validator.validate(dto);
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.room.description.size}")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.nullable.blank.description.}")));
    }

    @Test
    public void testInvalidCapacity() {
        UpdateHotelRoomRequestDto dto = new UpdateHotelRoomRequestDto(
                101,
                "Deluxe",
                "A spacious room with a king-sized bed",
                -1,
                150.0,
                null,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<UpdateHotelRoomRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.room.capacity.positive}")));
    }

    @Test
    public void testInvalidPricePerNight() {
        UpdateHotelRoomRequestDto dto = new UpdateHotelRoomRequestDto(
                101,
                "Deluxe",
                "A spacious room with a king-sized bed",
                2,
                -150.0,
                null,
                Collections.singleton("http://example.com/image.jpg")
        );

        Set<ConstraintViolation<UpdateHotelRoomRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessageTemplate().equals("{error.room.price.positive}")));
    }
}