package com.akm.hotelmanagement.service;

import com.akm.hotelmanagement.dto.user.CreateUserRequestDto;
import com.akm.hotelmanagement.dto.user.UpdateUserRequestDto;
import com.akm.hotelmanagement.dto.user.UserResponseDto;
import com.akm.hotelmanagement.entity.User;
import com.akm.hotelmanagement.entity.util.UserRole;
import com.akm.hotelmanagement.exception.ResourceAlreadyExistsException;
import com.akm.hotelmanagement.exception.ResourceNotFoundException;
import com.akm.hotelmanagement.filter.UserSpecifications;
import com.akm.hotelmanagement.repository.UserRepository;
import com.akm.hotelmanagement.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto createUser(CreateUserRequestDto userCreatDto) {
        if (userRepository.existsByUsername(userCreatDto.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already exists with username: " + userCreatDto.getUsername());
        }
        if (userRepository.existsByEmail(userCreatDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists with email: " + userCreatDto.getEmail());
        }
        User user = UserMapper.toEntity(userCreatDto);
        user.setPassword(passwordEncoder.encode(userCreatDto.getPassword()));
        user.setRole(UserRole.USER);
        user.setEnabled(true);
        return UserMapper.toResponseDto(userRepository.save(user));
    }

    public UserResponseDto getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserMapper::toResponseDto)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public UserResponseDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserMapper::toResponseDto)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public Page<UserResponseDto> getAllUsers(Pageable pageable, String filterBy, String filterValue) {
        if (filterBy == null || filterValue == null) {
            return userRepository.findAll(pageable)
                    .map(UserMapper::toResponseDto);
        }
        return userRepository.findAll(
                where(UserSpecifications.hasFilter(filterBy, filterValue)),
                pageable
        ).map(UserMapper::toResponseDto);
    }

    public Page<UserResponseDto> getAdmins(Pageable pageable, String filterBy, String filterValue) {
        if (filterBy == null || filterValue == null) {
            return userRepository.findAll(
                    where(UserSpecifications.hasFilter("role", UserRole.ADMIN.name())),
                    pageable
            ).map(UserMapper::toResponseDto);
        }
        return userRepository.findAll(
                where(UserSpecifications.hasFilter("role", UserRole.ADMIN.name()).and(
                        where(UserSpecifications.hasFilter(filterBy, filterValue))
                )),
                pageable
        ).map(UserMapper::toResponseDto);
    }

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

        if (!isPut) {
            checkForDuplicateFields(dto, user);
            updateUserFields(dto, user);
            return UserMapper.toResponseDto(userRepository.save(user));
        }
        // todo: check if another user exists with email or password provided in dto
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        return UserMapper.toResponseDto(userRepository.save(user));
    }

    public void deleteUser(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new ResourceNotFoundException("User not found with username " + username);
        }
        userRepository.deleteByUsername(username);
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
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
