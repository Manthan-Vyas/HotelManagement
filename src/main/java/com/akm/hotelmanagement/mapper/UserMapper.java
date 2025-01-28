package com.akm.hotelmanagement.mapper;

import com.akm.hotelmanagement.dto.user.CreateUserRequestDto;
import com.akm.hotelmanagement.dto.user.UpdateUserRequestDto;
import com.akm.hotelmanagement.dto.user.UserResponseDto;
import com.akm.hotelmanagement.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

import java.util.stream.Collectors;

public class UserMapper {
    public static CreateUserRequestDto toCreateDto(@NotNull User user) {
        return new CreateUserRequestDto(
                user.getName(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.getPhone()
        );
    }

    public static UpdateUserRequestDto toUpdateDto(@NotNull User user) {
        return new UpdateUserRequestDto(
                user.getName(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.getPhone()
        );
    }

    public static UserResponseDto toResponseDto(@NotNull User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUsername(),
                user.getPhone(),
                user.getReservations().stream().map(ReservationMapper::toResponseDto).collect(Collectors.toSet())
        );
    }

    public static User toEntity(@NotNull CreateUserRequestDto userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());
        return user;
    }

    public static User toEntity(@NotNull UpdateUserRequestDto userDTO, @Nullable User user) {
        if (user == null) {
            user = new User();
        }
        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getPassword() != null) {
            user.setPassword(userDTO.getPassword());
        }
        if (userDTO.getPhone() != null) {
            user.setPhone(userDTO.getPhone());
        }
        return user;
    }

    public static User toEntity(@NotNull UserResponseDto userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPhone(userDTO.getPhone());
        user.setReservations(userDTO.getReservations().stream().map(ReservationMapper::toEntity).collect(Collectors.toSet()));
        return user;
    }
}