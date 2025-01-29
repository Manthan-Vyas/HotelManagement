package com.akm.hotelmanagement.controller;

import com.akm.hotelmanagement.assembler.models.*;
import com.akm.hotelmanagement.dto.amenity.UpdateAmenityRequestDto;
import com.akm.hotelmanagement.dto.hotel.UpdateHotelRequestDto;
import com.akm.hotelmanagement.dto.user.UpdateUserRequestDto;
import com.akm.hotelmanagement.service.*;
import com.akm.hotelmanagement.wrapper.PagedResponse;
import com.akm.hotelmanagement.wrapper.ResponseWrapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin operations")
public class AdminController {

    private final UserService userService;
    private final HotelService hotelService;
    private final AmenityService amenityService;
    private final RoomService roomService;
    private final ReservationService reservationService;

    @GetMapping("/users")
    public ResponseEntity<ResponseWrapper<PagedResponse<UserModel>>> getAllUsers() {
        return null;
    }

    @GetMapping("/hotels")
    public ResponseEntity<ResponseWrapper<PagedResponse<HotelModel>>> getAllHotels() {
        return null;
    }

    @GetMapping("/rooms")
    public ResponseEntity<ResponseWrapper<PagedResponse<RoomModel>>> getAllRooms() {
        return null;
    }

    @GetMapping("/amenities")
    public ResponseEntity<ResponseWrapper<PagedResponse<AmenityModel>>> getAllAmenities() {
        return null;
    }

    @GetMapping("/reservations")
    public ResponseEntity<ResponseWrapper<PagedResponse<ReservationModel>>> getAllReservations() {
        return null;
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<ResponseWrapper<UserModel>> getUserDetails(@PathVariable UUID username) {
        return null;
    }

    @GetMapping("/hotels/{hotelId}")
    public ResponseEntity<ResponseWrapper<HotelModel>> getHotelDetails(@PathVariable Long hotelId) {
        return null;
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<ResponseWrapper<RoomModel>> getRoomDetails(@PathVariable Long roomId) {
        return null;
    }

    @GetMapping("/amenities/{amenityId}")
    public ResponseEntity<ResponseWrapper<AmenityModel>> getAmenityDetails(@PathVariable Long amenityId) {
        return null;
    }

    @GetMapping("/reservations/{reservationId}")
    public ResponseEntity<ResponseWrapper<ReservationModel>> getReservationDetails(@PathVariable Long reservationId) {
        return null;
    }

    @PutMapping("/users/{username}")
    public ResponseEntity<ResponseWrapper<UserModel>> updateUserDetails(@PathVariable UUID username, @RequestBody UpdateUserRequestDto dto) {
        return null;
    }

    @PatchMapping("/users/{username}")
    public ResponseEntity<ResponseWrapper<UserModel>> updateUser(@PathVariable UUID username, @RequestBody UpdateUserRequestDto dto) {
        return null;
    }

    @PutMapping("/hotels/{hotelId}")
    public ResponseEntity<ResponseWrapper<HotelModel>> updateHotelDetails(@PathVariable Long hotelId, @RequestBody UpdateHotelRequestDto dto) {
        return null;
    }

    @PatchMapping("/hotels/{hotelId}")
    public ResponseEntity<ResponseWrapper<HotelModel>> updateHotel(@PathVariable Long hotelId, @RequestBody UpdateHotelRequestDto dto) {
        return null;
    }

    @PutMapping("/amenities/{amenityId}")
    public ResponseEntity<ResponseWrapper<AmenityModel>> updateAmenityDetails(@PathVariable Long amenityId, @RequestBody UpdateAmenityRequestDto dto) {
        return null;
    }

    @PatchMapping("/amenities/{amenityId}")
    public ResponseEntity<ResponseWrapper<AmenityModel>> updateAmenity(@PathVariable Long amenityId, @RequestBody UpdateAmenityRequestDto dto) {
        return null;
    }

    @PatchMapping("/users/{username}")
    public ResponseEntity<ResponseWrapper<UserModel>> disableUser(@PathVariable String username) {
        return null;
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<ResponseWrapper<UserModel>> deleteUser(@PathVariable String username) {
        return null;
    }

    @DeleteMapping("/hotels/{hotelId}")
    public ResponseEntity<ResponseWrapper<HotelModel>> deleteHotel(@PathVariable Long hotelId) {
        return null;
    }

    @DeleteMapping("/amenities/{amenityId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteAmenity(@PathVariable Long amenityId) {
        return null;
    }
}