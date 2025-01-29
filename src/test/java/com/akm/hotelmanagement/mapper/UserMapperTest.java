package com.akm.hotelmanagement.mapper;

import com.akm.hotelmanagement.dto.user.CreateUserRequestDto;
import com.akm.hotelmanagement.dto.user.UpdateUserRequestDto;
import com.akm.hotelmanagement.dto.user.UserResponseDto;
import com.akm.hotelmanagement.entity.User;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    @Test
    void testToCreateDto() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setUsername("johndoe");
        user.setPassword("password123");
        user.setPhone("1234567890");

        CreateUserRequestDto dto = UserMapper.toCreateDto(user);

        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getUsername(), dto.getUsername());
        assertEquals(user.getPassword(), dto.getPassword());
        assertEquals(user.getPhone(), dto.getPhone());
    }

    @Test
    void testToUpdateDto() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setUsername("johndoe");
        user.setPassword("password123");
        user.setPhone("1234567890");

        UpdateUserRequestDto dto = UserMapper.toUpdateDto(user);

        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getUsername(), dto.getUsername());
        assertEquals(user.getPassword(), dto.getPassword());
        assertEquals(user.getPhone(), dto.getPhone());
    }

    @Test
    void testToResponseDto() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setUsername("johndoe");
        user.setPhone("1234567890");
        user.setReservations(new HashSet<>());

        UserResponseDto dto = UserMapper.toResponseDto(user);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getUsername(), dto.getUsername());
        assertEquals(user.getPhone(), dto.getPhone());
        assertEquals(user.getReservations().size(), dto.getReservations().size());
    }

    @Test
    void testToEntityFromCreateDto() {
        CreateUserRequestDto dto = new CreateUserRequestDto(
                "John Doe",
                "john.doe@example.com",
                "johndoe",
                "password123",
                "1234567890"
        );

        User user = UserMapper.toEntity(dto);

        assertEquals(dto.getName(), user.getName());
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getUsername(), user.getUsername());
        assertEquals(dto.getPassword(), user.getPassword());
        assertEquals(dto.getPhone(), user.getPhone());
    }

    @Test
    void testToEntityFromUpdateDto() {
        UpdateUserRequestDto dto = new UpdateUserRequestDto(
                "John Doe",
                "john.doe@example.com",
                "johndoe",
                "password123",
                "1234567890"
        );

        User user = new User();
        user.setName("Old Name");
        user.setEmail("old.email@example.com");
        user.setUsername("oldusername");
        user.setPassword("oldpassword");
        user.setPhone("0987654321");

        User updatedUser = UserMapper.toEntity(dto, user);

        assertEquals(dto.getName(), updatedUser.getName());
        assertEquals(dto.getEmail(), updatedUser.getEmail());
        assertEquals(dto.getUsername(), updatedUser.getUsername());
        assertEquals(dto.getPassword(), updatedUser.getPassword());
        assertEquals(dto.getPhone(), updatedUser.getPhone());
    }

    @Test
    void testToEntityFromResponseDto() {
        UserResponseDto dto = new UserResponseDto(
                UUID.randomUUID(),
                "John Doe",
                "john.doe@example.com",
                "johndoe",
                "1234567890",
                new HashSet<>()
        );

        User user = UserMapper.toEntity(dto);

        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getName(), user.getName());
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getUsername(), user.getUsername());
        assertEquals(dto.getPhone(), user.getPhone());
        assertEquals(dto.getReservations().size(), user.getReservations().size());
    }
}