package com.akm.hotelmanagement.dto.user;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.akm.hotelmanagement.entity.User}
 */
@Value
public class CreateUserRequestDto implements Serializable {
    @NotBlank(message = "Name is mandatory")
    String name;
    @NotBlank(message = "Email is mandatory")
    @Email(regexp = ".*@.*\\..*", message = "Invalid email address")
    String email;
    @NotBlank(message = "Username is mandatory")
    String username;
    @NotBlank(message = "Password is mandatory")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters long, contain at least one digit, one lowercase letter, one uppercase letter, and one special character"
    )
    String password;
    @NotBlank(message = "Phone is mandatory")
    @Digits(integer = 10, fraction = 0, message = "Phone number must be a 10-digit number")
    String phone;
}