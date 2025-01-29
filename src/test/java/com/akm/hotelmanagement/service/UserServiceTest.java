package com.akm.hotelmanagement.service;

import com.akm.hotelmanagement.dto.user.CreateUserRequestDto;
import com.akm.hotelmanagement.dto.user.UpdateUserRequestDto;
import com.akm.hotelmanagement.dto.user.UserResponseDto;
import com.akm.hotelmanagement.entity.User;
import com.akm.hotelmanagement.entity.util.UserRole;
import com.akm.hotelmanagement.exception.ResourceNotFoundException;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private CreateUserRequestDto createUserRequestDto;
    private UpdateUserRequestDto updateUserRequestDto;
    private User user;

    @BeforeEach
    void setUp() {
        createUserRequestDto = new CreateUserRequestDto(
                "Test",
                "testuser@example.com",
                "testuser",
                "password",
                "123123123"
        );

        updateUserRequestDto = new UpdateUserRequestDto(
        "User",
        "updateduser@example.com",
        "updateduser",
        "password",
        "1123213123"
        );

        user = new User();
        user.setUsername("Test");
        user.setEmail("testuser@example.com");
        user.setUsername("testuser");
        user.setPassword("password");
        user.setPhone("123123123");
        user.setRole(UserRole.USER);
        user.setEnabled(true);
    }

    @Test
    void testCreateUser() {
        // Arrange
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto response = userService.createUser(createUserRequestDto);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserByUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

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

        Page<UserResponseDto> response = userService.getAllUsers(pageable, null, null);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    void testGetAdmins() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(Collections.singletonList(user));
        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(userPage);

        Page<UserResponseDto> response = userService.getAdmins(pageable, null, null);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    void testUpdateUser() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto response = userService.updateUser("testuser", updateUserRequestDto, true);

        assertNotNull(response);
        assertEquals("updateduser", response.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
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
