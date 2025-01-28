package com.akm.hotelmanagement.dto.user;

import com.akm.hotelmanagement.validation.NullableNotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.akm.hotelmanagement.entity.User}
 */
@Value
public class UpdateUserRequestDto implements Serializable {
    @NullableNotBlank(message = "{error.nullable.blank.name}")
    @Size(min = 5, max = 20, message = "{error.user.name.size}")
    String name;
    @NullableNotBlank(message = "{error.nullable.blank.email}")
    @Email(regexp = ".*@.*\\..*", message = "{error.invalid.email}")
    String email;
    @NullableNotBlank(message = "{error.nullable.blank.username}")
    @Size(min = 5, max = 20, message = "{error.user.username.size}")
    String username;
    @NullableNotBlank(message = "{error.nullable.blank.password}")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "{error.invalid.password.pattern}"
    )
    String password;
    @NullableNotBlank(message = "{error.nullable.blank.phone}")
    @Pattern(regexp = "^\\d{10}$", message = "{error.invalid.phone.pattern}")
    String phone;
}