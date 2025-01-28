package com.akm.hotelmanagement.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.akm.hotelmanagement.entity.User}
 */
@Value
public class CreateUserRequestDto implements Serializable {
    @NotBlank(message = "{error.required.name}")
    @Size(min = 2, max = 50, message = "{error.user.name.size}")
    String name;
    @NotBlank(message = "{error.required.email}")
    @Email(regexp = ".*@.*\\..*", message = "{error.invalid.email}")
    String email;
    @NotBlank(message = "{error.required.username}")
    @Size(min = 3, max = 20, message = "{error.user.username.size}")
    String username;
    @NotBlank(message = "{error.required.password}")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "{error.invalid.password.pattern}"
    )
    String password;
    @NotBlank(message = "{error.required.phone}")
    @Pattern(regexp = "^\\d{10}$", message = "{error.invalid.phone.pattern}")
    String phone;
}