package com.akm.hotelmanagement.dto.amenity;

import com.akm.hotelmanagement.dto.user.UpdateUserRequestDtoTest;
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

public class UpdateAmenityRequestDtoTest {
    private static Validator validator;

    @BeforeAll
    static void setup() {
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        } catch (Exception e) {
            Logger.getLogger(UpdateUserRequestDtoTest.class.getName()).severe(e.getMessage());
        }
    }

    @Test
    public void testValidUpdateAmenityRequestDto() {
        List<UpdateAmenityRequestDto> dtoList = List.of(
                new UpdateAmenityRequestDto(
                "Pool",
                "A large swimming pool"
                ),
                new UpdateAmenityRequestDto(
                        null,
                        null
                )
        );

        List<Set<ConstraintViolation<UpdateAmenityRequestDto>>> violationsList = dtoList.stream()
                .map(dto -> validator.validate(dto))
                .toList();

        violationsList.forEach(violations -> assertTrue(violations.isEmpty()));
    }

    @Test
    public void testInvalidName() {
        UpdateAmenityRequestDto dto = new UpdateAmenityRequestDto(
                "  ",
                "A large swimming pool"
        );

        Set<ConstraintViolation<UpdateAmenityRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("{error.nullable.blank.name}", violations.iterator().next().getMessageTemplate());
    }

    @Test
    public void testInvalidDescription() {
        UpdateAmenityRequestDto dto = new UpdateAmenityRequestDto(
                "Pool",
                "qwe"
        );

        Set<ConstraintViolation<UpdateAmenityRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("{error.amenity.description.size}", violations.iterator().next().getMessageTemplate());
    }
}