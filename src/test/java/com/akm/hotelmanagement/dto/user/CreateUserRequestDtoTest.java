package com.akm.hotelmanagement.dto.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;

import java.util.Set;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ImportResource("classpath:messages")
public class CreateUserRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        } catch (Exception e) {
            Logger.getLogger(CreateUserRequestDtoTest.class.getName()).severe(e.getMessage());
        }
    }

    @Test
    public void testValidCreateUserRequestDto() {
        CreateUserRequestDto dto = new CreateUserRequestDto(
                "John Doe",
                "john.doe@example.com",
                "johndoe",
                "Password@123",
                "1234567890"
        );

        Set<ConstraintViolation<CreateUserRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidName() {
        CreateUserRequestDto dto = new CreateUserRequestDto(
                "J",
                "john.doe@example.com",
                "johndoe",
                "Password@123",
                "1234567890"
        );

        Set<ConstraintViolation<CreateUserRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("{error.user.name.size}", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidEmail() {
        CreateUserRequestDto dto = new CreateUserRequestDto(
                "John Doe",
                "invalid-email",
                "johndoe",
                "Password@123",
                "1234567890"
        );

        Set<ConstraintViolation<CreateUserRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("{error.invalid.email}", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidUsername() {
        CreateUserRequestDto dto = new CreateUserRequestDto(
                "John Doe",
                "john.doe@example.com",
                "jo",
                "Password@123",
                "1234567890"
        );

        Set<ConstraintViolation<CreateUserRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("{error.user.username.size}", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidPassword() {
        CreateUserRequestDto dto = new CreateUserRequestDto(
                "John Doe",
                "john.doe@example.com",
                "johndoe",
                "password",
                "1234567890"
        );

        Set<ConstraintViolation<CreateUserRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("{error.invalid.password.pattern}", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidPhone() {
        CreateUserRequestDto dto = new CreateUserRequestDto(
                "John Doe",
                "john.doe@example.com",
                "johndoe",
                "Password@123",
                "12345"
        );

        Set<ConstraintViolation<CreateUserRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("{error.invalid.phone.pattern}", violations.iterator().next().getMessage());
    }

}