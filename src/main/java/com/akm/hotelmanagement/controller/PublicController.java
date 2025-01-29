package com.akm.hotelmanagement.controller;

import com.akm.hotelmanagement.assembler.models.HotelModel;
import com.akm.hotelmanagement.assembler.models.RoomModel;
import com.akm.hotelmanagement.assembler.models.UserModel;
import com.akm.hotelmanagement.dto.user.CreateUserRequestDto;
import com.akm.hotelmanagement.service.HotelService;
import com.akm.hotelmanagement.service.RoomService;
import com.akm.hotelmanagement.service.UserService;
import com.akm.hotelmanagement.wrapper.PagedResponse;
import com.akm.hotelmanagement.wrapper.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Tag(name = "Public Controller", description = "Endpoints accessible to the public")
public class PublicController {

    private final UserService userService;
    private final HotelService hotelService;
    private final RoomService roomService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Register a new user with the provided details")
    public ResponseEntity<ResponseWrapper<UserModel>> registerUser(@Valid @RequestBody CreateUserRequestDto userDto) {
        return null;
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user", description = "Login a user with the provided details")
    public void loginUser() {

    }

    @GetMapping("/hotels")
    public ResponseEntity<ResponseWrapper<PagedResponse<HotelModel>>> getAllHotels() {
        return null;
    }

    @GetMapping("/rooms")
    public ResponseEntity<ResponseWrapper<PagedResponse<RoomModel>>> getAllRooms() {
        return null;
    }
}
