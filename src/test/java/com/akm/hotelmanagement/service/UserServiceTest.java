package com.akm.hotelmanagement.service;

import com.akm.hotelmanagement.dto.user.CreateUserRequestDto;
import com.akm.hotelmanagement.dto.user.UpdateUserRequestDto;
import com.akm.hotelmanagement.dto.user.UserResponseDto;
import com.akm.hotelmanagement.entity.User;
import com.akm.hotelmanagement.entity.util.UserRole;
import com.akm.hotelmanagement.exception.ResourceAlreadyExistsException;
import com.akm.hotelmanagement.exception.ResourceNotFoundException;
import com.akm.hotelmanagement.mapper.UserMapper;
import com.akm.hotelmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private CreateUserRequestDto createUserRequestDto;
    private UpdateUserRequestDto updateUserRequestDto;
    private User user;

    @BeforeEach
    void setUp() {
        createUserRequestDto = new CreateUserRequestDto(
                "Test User",
                "testuser@example.com",
                "testuser",
                "password",
                "123123123"
        );

        updateUserRequestDto = new UpdateUserRequestDto(
                "Updated User",
                "updateduser@example.com",
                "updateduser",
                "newpassword",
                "1123213123"
        );

        user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Test User");
        user.setEmail("testuser@example.com");
        user.setUsername("testuser");
        user.setPassword("password");
        user.setPhone("123123123");
        user.setRole(UserRole.USER);
        user.setEnabled(true);
    }

    @Test
    void testCreateUser() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userMapper.toEntity(any(CreateUserRequestDto.class))).thenReturn(user); // Mocking userMapper.toEntity
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(UUID.randomUUID()); // Ensure the user has an ID
            return user;
        });
        when(userMapper.toResponseDto(any(User.class))).thenReturn(new UserResponseDto(UUID.randomUUID(), "Test User", "testuser@example.com", "testuser", "123123123", new HashSet<>()));

        UserResponseDto response = userService.createUser(createUserRequestDto);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_ShouldThrowException_WhenUsernameExists() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> userService.createUser(createUserRequestDto));
    }

    @Test
    void testCreateUser_ShouldThrowException_WhenEmailExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> userService.createUser(createUserRequestDto));
    }

    @Test
    void testGetUserByUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userMapper.toResponseDto(any(User.class))).thenReturn(new UserResponseDto(UUID.randomUUID(), "Test User", "testuser@example.com", "testuser", "123123123", new HashSet<>()));

        UserResponseDto response = userService.getUserByUsername("testuser");

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
    }

    @Test
    void testGetUserByUsername_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByUsername("testuser"));
    }

    @Test
    void testGetUserByEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userMapper.toResponseDto(any(User.class))).thenReturn(new UserResponseDto(UUID.randomUUID(), "Test User", "testuser@example.com", "testuser", "123123123", new HashSet<>()));

        UserResponseDto response = userService.getUserByEmail("testuser@example.com");

        assertNotNull(response);
        assertEquals("testuser@example.com", response.getEmail());
    }

    @Test
    void testGetUserByEmail_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByEmail("testuser@example.com"));
    }

    @Test
    void testGetAllUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(Collections.singletonList(user));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);
        when(userMapper.toResponseDto(any(User.class))).thenReturn(new UserResponseDto(UUID.randomUUID(), "Test User", "testuser@example.com", "testuser", "123123123", new HashSet<>()));

        Page<UserResponseDto> response = userService.getAllUsers(pageable, null, null);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    void testUpdateUser() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDto(any(User.class))).thenReturn(new UserResponseDto(UUID.randomUUID(), "Updated User", "updateduser@example.com", "updateduser", "1123213123", new HashSet<>()));

        UserResponseDto response = userService.updateUser("testuser", updateUserRequestDto, true);

        assertNotNull(response);
        assertEquals("updateduser", response.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.updateUser("testuser", updateUserRequestDto, true));
    }

    @Test
    void testDeleteUser() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        doNothing().when(userRepository).deleteByUsername(anyString());

        userService.deleteUser("testuser");

        verify(userRepository, times(1)).deleteByUsername(anyString());
    }

    @Test
    void testDeleteUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser("testuser"));
    }
}