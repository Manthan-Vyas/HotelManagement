package com.akm.hotelmanagement.dto.amenity;

import com.akm.hotelmanagement.dto.user.CreateUserRequestDtoTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateAmenityRequestDtoTest {
    private static Validator validator;

    @BeforeAll
    static void setup() {
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        } catch (Exception e) {
            Logger.getLogger(CreateUserRequestDtoTest.class.getName()).severe(e.getMessage());
        }
    }

    @Test
    public void testValidCreateAmenityRequestDto() {
        CreateAmenityRequestDto dto = new CreateAmenityRequestDto(
                "Pool",
                "A large swimming pool"
        );

        Set<ConstraintViolation<CreateAmenityRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidName() {
        CreateAmenityRequestDto dto = new CreateAmenityRequestDto(
                null,
                "A large swimming pool"
        );

        Set<ConstraintViolation<CreateAmenityRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("{error.required.name}", violations.iterator().next().getMessageTemplate());
    }

    @Test
    public void testInvalidDescription() {
        CreateAmenityRequestDto dto = new CreateAmenityRequestDto(
                "Pool",
                "qwe"
        );

        Set<ConstraintViolation<CreateAmenityRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("{error.amenity.description.size}", violations.iterator().next().getMessageTemplate());
    }
}