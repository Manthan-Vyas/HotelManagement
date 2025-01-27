package com.akm.hotelmanagement.dto.user;

import com.akm.hotelmanagement.validation.NullableNotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.akm.hotelmanagement.entity.User}
 */
@Value
public class UpdateUserRequestDto implements Serializable {
    @NullableNotBlank(message = "Name should either be null or not blank")
    String name;
    @NullableNotBlank(message = "Email should either be null or not blank")
    @Email(regexp = ".*@.*\\..*", message = "Invalid email address")
    String email;
    @NullableNotBlank(message = "Username should either be null or not blank")
    String username;
    @NullableNotBlank(message = "Password should either be null or not blank")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters long, contain at least one digit, one lowercase letter, one uppercase letter, and one special character"
    )
    String password;
    @NullableNotBlank(message = "Phone should either be null or not blank")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be a 10-digit number")
    String phone;
}