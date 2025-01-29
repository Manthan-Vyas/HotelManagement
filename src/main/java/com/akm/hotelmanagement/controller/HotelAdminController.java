package com.akm.hotelmanagement.controller;

import com.akm.hotelmanagement.assembler.models.HotelModel;
import com.akm.hotelmanagement.assembler.models.ReservationModel;
import com.akm.hotelmanagement.assembler.models.RoomModel;
import com.akm.hotelmanagement.dto.hotel.UpdateHotelRequestDto;
import com.akm.hotelmanagement.dto.room.UpdateHotelRoomRequestDto;
import com.akm.hotelmanagement.service.HotelService;
import com.akm.hotelmanagement.service.RoomService;
import com.akm.hotelmanagement.wrapper.PagedResponse;
import com.akm.hotelmanagement.wrapper.ResponseWrapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/hotel-admin")
@RequiredArgsConstructor
@Tag(name = "Hotel Admin", description = "Operations pertaining to hotel administration")
public class HotelAdminController {

    private final HotelService hotelService;
    private final RoomService roomService;

    @PostMapping("/hotels/{hotelId}/rooms")
    public ResponseEntity<ResponseWrapper<RoomModel>> addHotelRoom(@PathVariable Long hotelId, @RequestBody UpdateHotelRequestDto dto) {
        return null;
    }

    @GetMapping("/hotels/{hotelId}")
    public ResponseEntity<ResponseWrapper<HotelModel>> getHotelDetails(@PathVariable Long hotelId) {
        return null;
    }

    @GetMapping("/hotels/{hotelId}/rooms")
    public ResponseEntity<ResponseWrapper<PagedResponse<RoomModel>>> getHotelRooms(@PathVariable Long hotelId) {
        return null;
    }

    @GetMapping("/hotels/{hotelId}/rooms/{roomId}")
    public ResponseEntity<ResponseWrapper<RoomModel>> getHotelRoomDetails(@PathVariable Long hotelId, @PathVariable Long roomId) {
        return null;
    }

    @GetMapping("/hotels/{hotelId}/rooms/{roomId}/reservations")
    public ResponseEntity<ResponseWrapper<PagedResponse<ReservationModel>>> getHotelRoomReservations(@PathVariable Long hotelId, @PathVariable Long roomId) {
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

    @PutMapping("/hotels/{hotelId}/rooms/{roomId}")
    public ResponseEntity<ResponseWrapper<RoomModel>> updateHotelRoomDetails(@PathVariable Long hotelId, @PathVariable Long roomId, @RequestBody UpdateHotelRoomRequestDto dto) {
        return null;
    }

    @PatchMapping("/hotels/{hotelId}/rooms/{roomId}")
    public ResponseEntity<ResponseWrapper<RoomModel>> updateHotelRoom(@PathVariable Long hotelId, @PathVariable Long roomId, @RequestBody UpdateHotelRoomRequestDto dto) {
        return null;
    }

    @DeleteMapping("/hotels/{hotelId}/rooms/{roomId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteHotelRoom(@PathVariable Long hotelId, @PathVariable Long roomId) {
        return null;
    }
}