package com.akm.hotelmanagement.service;

import com.akm.hotelmanagement.dto.user.CreateUserRequestDto;
import com.akm.hotelmanagement.dto.user.UpdateUserRequestDto;
import com.akm.hotelmanagement.dto.user.UserResponseDto;
import com.akm.hotelmanagement.entity.User;
import com.akm.hotelmanagement.entity.util.UserRole;
import com.akm.hotelmanagement.exception.ResourceAlreadyExistsException;
import com.akm.hotelmanagement.exception.ResourceNotFoundException;
import com.akm.hotelmanagement.filter.UserSpecifications;
import com.akm.hotelmanagement.mapper.UserMapper;
import com.akm.hotelmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDto createUser(CreateUserRequestDto userCreatDto) {
        if (userRepository.existsByUsername(userCreatDto.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already exists with username: " + userCreatDto.getUsername());
        }
        if (userRepository.existsByEmail(userCreatDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists with email: " + userCreatDto.getEmail());
        }
        User user = userMapper.toEntity(userCreatDto);
        user.setPassword(passwordEncoder.encode(userCreatDto.getPassword()));
        user.setRole(UserRole.USER);
        user.setEnabled(true);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toResponseDto)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toResponseDto)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Transactional
    public UserResponseDto getUserByReservationId(Long reservationId) {
        return userMapper.toResponseDto(
                userRepository.findAll(
                        where(UserSpecifications.hasFilter("reservation-id", reservationId.toString()))
                ).stream().findFirst().orElseThrow(
                        () -> new ResourceNotFoundException("User not found with reservation id " + reservationId)
                )
        );
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllUsers(Pageable pageable, String filterBy, String filterValue) {
        if (filterBy == null || filterValue == null) {
            return userRepository.findAll(pageable)
                    .map(userMapper::toResponseDto);
        }
        return userRepository.findAll(
                where(UserSpecifications.hasFilter(filterBy, filterValue)),
                pageable
        ).map(userMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAdmins(Pageable pageable, String filterBy, String filterValue) {
        if (filterBy == null || filterValue == null) {
            return userRepository.findAll(
                    where(UserSpecifications.hasFilter("role", UserRole.ADMIN.name())),
                    pageable
            ).map(userMapper::toResponseDto);
        }
        return userRepository.findAll(
                where(UserSpecifications.hasFilter("role", UserRole.ADMIN.name()).and(
                        where(UserSpecifications.hasFilter(filterBy, filterValue))
                )),
                pageable
        ).map(userMapper::toResponseDto);
    }

    @Transactional
    public UserResponseDto updateUser(String username, UpdateUserRequestDto dto, boolean isPut) {
        if (isPut && dto.hasAnyFieldNull()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "All fields are required for PUT request"
            );
        }
        if (!isPut && dto.hasAllFieldsNull()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "At least one field is required for PATCH request"
            );
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (isPut) {
            if (areAllFieldsEqual(dto, user)) {
                throw new ResourceAlreadyExistsException("All fields are same as the existing one");
            }
        } else {
            checkForDuplicateFields(dto, user);
        }

        updateUserFields(dto, user);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new ResourceNotFoundException("User not found with username " + username);
        }
        userRepository.deleteByUsername(username);
    }

    @Transactional
    public UserResponseDto changeUserEnabled(String username, boolean enabled) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username " + username));
        user.setEnabled(enabled);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Transactional
    public boolean changeUserPassword(String emailOrUsername, @Nullable String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(emailOrUsername)
                .orElseGet(() -> userRepository.findByEmail(emailOrUsername)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with username or email: " + emailOrUsername)));

        if (oldPassword != null && !passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private void checkForDuplicateFields(UpdateUserRequestDto dto, User user) {
        if (dto.getName() != null && dto.getName().equals(user.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Name is same as the existing one " + dto.getName()
            );
        }
        if (dto.getEmail() != null && dto.getEmail().equals(user.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email is same as the existing one " + dto.getEmail()
            );
        }
        if (dto.getUsername() != null && dto.getUsername().equals(user.getUsername())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username is same as the existing one " + dto.getUsername()
            );
        }
        if (dto.getPassword() != null && passwordEncoder.encode(dto.getPassword()).equals(user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password is same as the existing one " + dto.getPassword()
            );
        }
        if (dto.getPhone() != null && dto.getPhone().equals(user.getPhone())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Phone number is same as the existing one " + dto.getPhone()
            );
        }
    }

    private boolean areAllFieldsEqual(UpdateUserRequestDto dto, User user) {
        return Objects.equals(dto.getUsername(), user.getUsername()) &&
                Objects.equals(dto.getName(), user.getName()) &&
                Objects.equals(dto.getEmail(), user.getEmail()) &&
                passwordEncoder.matches(dto.getPassword(), user.getPassword()) &&
                Objects.equals(dto.getPhone(), user.getPhone());
    }

    private void updateUserFields(UpdateUserRequestDto dto, User user) {
        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
        if (!user.isEnabled()) {
            throw new UsernameNotFoundException("User is disabled with username: " + username);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(String.valueOf(user.getRole()))
                .build();
    }
}