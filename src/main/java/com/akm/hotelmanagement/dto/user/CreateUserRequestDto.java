package com.akm.hotelmanagement.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

import static com.akm.hotelmanagement.util.Constants.*;

/**
 * DTO for {@link com.akm.hotelmanagement.entity.User}
 */
@Value
public class CreateUserRequestDto implements Serializable {
    @NotBlank(message = "{error.required.name}")
    @Size(min = USER_NAME_MIN_LENGTH, max = USER_NAME_MAX_LENGTH, message = "{error.user.name.size}")
    @Pattern(regexp = NAME_PATTERN, message = "{error.invalid.name.pattern}")
    String name;
    @NotBlank(message = "{error.required.email}")
    @Pattern(regexp = EMAIL_PATTERN, message = "{error.invalid.email}")
    String email;
    @NotBlank(message = "{error.required.username}")
    @Size(min = USER_USERNAME_MIN_LENGTH, max = USER_USERNAME_MAX_LENGTH, message = "{error.user.username.size}")
    @Pattern(regexp = USERNAME_PATTERN, message = "{error.invalid.username.pattern}")
    String username;
    @NotBlank(message = "{error.required.password}")
    @Pattern(
            regexp = PASSWORD_PATTERN,
            message = "{error.invalid.password.pattern}"
    )
    String password;
    @NotBlank(message = "{error.required.phone}")
    @Pattern(regexp = PHONE_PATTERN, message = "{error.invalid.phone.pattern}")
    String phone;
}