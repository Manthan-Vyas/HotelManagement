package com.akm.hotelmanagement.dto.user;

import com.akm.hotelmanagement.validation.NullableNotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

import static com.akm.hotelmanagement.util.Constants.*;

/**
 * DTO for {@link com.akm.hotelmanagement.entity.User}
 */
@Value
public class UpdateUserRequestDto implements Serializable {
    @NullableNotBlank(message = "{error.nullable.blank.name}")
    @Size(min = USER_NAME_MIN_LENGTH, max = USER_NAME_MAX_LENGTH, message = "{error.user.name.size}")
    @Pattern(regexp = NAME_PATTERN, message = "{error.invalid.name.pattern}")
    String name;
    @NullableNotBlank(message = "{error.nullable.blank.email}")
    @Pattern(regexp = EMAIL_PATTERN, message = "{error.invalid.email}")
    String email;
    @NullableNotBlank(message = "{error.nullable.blank.username}")
    @Size(min = USER_USERNAME_MIN_LENGTH, max = USER_USERNAME_MAX_LENGTH, message = "{error.user.username.size}")
    @Pattern(regexp = USERNAME_PATTERN, message = "{error.invalid.username.pattern}")
    String username;
    @NullableNotBlank(message = "{error.nullable.blank.password}")
    @Pattern(
            regexp = PASSWORD_PATTERN,
            message = "{error.invalid.password.pattern}"
    )
    String password;
    @NullableNotBlank(message = "{error.nullable.blank.phone}")
    @Pattern(regexp = PHONE_PATTERN, message = "{error.invalid.phone.pattern}")
    String phone;

    public boolean hasAllFieldsNull() {
        return this.getName() == null && this.getUsername() == null && this.getEmail() == null && this.getPassword() == null && this.getPhone() == null;
    }

    public boolean hasAllFieldsNotNull() {
        return this.getName() != null && this.getEmail() != null && this.getPassword() != null && this.getPhone() != null;
    }

    public boolean hasAnyFieldNotNull() {
        return this.getName() != null || this.getEmail() != null || this.getPassword() != null || this.getPhone() != null;
    }

    public boolean hasAnyFieldNull() {
        return this.getName() == null || this.getEmail() == null || this.getPassword() == null || this.getPhone() == null;
    }
}