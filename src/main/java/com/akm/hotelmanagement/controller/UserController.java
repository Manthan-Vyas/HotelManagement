package com.akm.hotelmanagement.controller;

import com.akm.hotelmanagement.assembler.models.HotelModel;
import com.akm.hotelmanagement.assembler.models.ReservationModel;
import com.akm.hotelmanagement.assembler.models.RoomModel;
import com.akm.hotelmanagement.assembler.models.UserModel;
import com.akm.hotelmanagement.dto.reservation.CreateOrUpdateUserRoomReservationRequestDto;
import com.akm.hotelmanagement.dto.user.UpdateUserRequestDto;
import com.akm.hotelmanagement.service.HotelService;
import com.akm.hotelmanagement.service.RoomService;
import com.akm.hotelmanagement.service.UserService;
import com.akm.hotelmanagement.wrapper.PagedResponse;
import com.akm.hotelmanagement.wrapper.ResponseWrapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Endpoints for user operations")
public class UserController {

    private final UserService userService;
    private final HotelService hotelService;
    private final RoomService roomService;

    @GetMapping("/{username}")
    public ResponseEntity<ResponseWrapper<UserModel>> getUserDetails(@PathVariable String username) {
        return null;
    }

    @PutMapping("/{username}")
    public ResponseEntity<ResponseWrapper<UserModel>> updateUserDetails(@PathVariable String username, @RequestBody UpdateUserRequestDto dto) {
        return null;
    }

    @GetMapping("/{username}/reservations")
    public ResponseEntity<ResponseWrapper<PagedResponse<ReservationModel>>> getUserReservations(@PathVariable String username) {
        return null;
    }

    @GetMapping("/{username}/reservations/{reservationId}")
    public ResponseEntity<ResponseWrapper<ResponseEntity<ResponseWrapper<ReservationModel>>>> getUserReservation(@PathVariable String username, @PathVariable Long reservationId) {
        return null;
    }

    @PostMapping("/{username}/reservations")
    public ResponseEntity<ResponseWrapper<ReservationModel>> bookReservation(@PathVariable String username, @RequestBody CreateOrUpdateUserRoomReservationRequestDto dto) {
        return null;
    }

    @PutMapping("/{username}/reservations/{reservationId}")
    public ResponseEntity<ResponseWrapper<ReservationModel>> updateReservation(@PathVariable String username, @PathVariable Long reservationId, @RequestBody CreateOrUpdateUserRoomReservationRequestDto dto) {
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
}