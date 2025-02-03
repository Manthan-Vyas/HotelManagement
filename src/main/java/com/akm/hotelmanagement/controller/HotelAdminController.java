package com.akm.hotelmanagement.controller;

import com.akm.hotelmanagement.assembler.AmenityModelAssembler;
import com.akm.hotelmanagement.assembler.HotelModelAssembler;
import com.akm.hotelmanagement.assembler.ReservationModelAssembler;
import com.akm.hotelmanagement.assembler.RoomModelAssembler;
import com.akm.hotelmanagement.assembler.models.HotelModel;
import com.akm.hotelmanagement.assembler.models.ReservationModel;
import com.akm.hotelmanagement.assembler.models.RoomModel;
import com.akm.hotelmanagement.controller.base.AdminBaseController;
import com.akm.hotelmanagement.dto.hotel.UpdateHotelRequestDto;
import com.akm.hotelmanagement.dto.room.CreateHotelRoomRequestDto;
import com.akm.hotelmanagement.dto.room.UpdateHotelRoomRequestDto;
import com.akm.hotelmanagement.entity.util.ReservationStatus;
import com.akm.hotelmanagement.entity.util.RoomStatus;
import com.akm.hotelmanagement.exception.ResourceAlreadyExistsException;
import com.akm.hotelmanagement.exception.ResourceNotFoundException;
import com.akm.hotelmanagement.service.AmenityService;
import com.akm.hotelmanagement.service.HotelService;
import com.akm.hotelmanagement.service.ReservationService;
import com.akm.hotelmanagement.service.RoomService;
import com.akm.hotelmanagement.wrapper.PagedResponse;
import com.akm.hotelmanagement.wrapper.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.akm.hotelmanagement.util.Constants.*;
import static com.akm.hotelmanagement.util.Utils.getPageable;

@Validated
@RestController
@RequestMapping("/hotel-admin")
@Tag(name = "Hotel Admin", description = "Operations pertaining to hotel administration")
public class HotelAdminController extends AdminBaseController {


    public HotelAdminController(AmenityService amenityService, AmenityModelAssembler amenityModelAssembler, HotelService hotelService, HotelModelAssembler hotelModelAssembler, RoomService roomService, RoomModelAssembler roomModelAssembler, ReservationService reservationService, ReservationModelAssembler reservationModelAssembler) {
        super(amenityService, amenityModelAssembler, hotelService, hotelModelAssembler, roomService, roomModelAssembler, reservationService, reservationModelAssembler);
    }

    @GetMapping
    @Operation(summary = "Hotel admin home", description = "Get the home page for hotel admin")
    public ResponseEntity<ResponseWrapper<String>> getHotelAdminHome() {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        "Welcome to the hotel admin home page",
                        null
                )
        );
    }

    @PostMapping("/hotels/{hotelId}/rooms")
    @Operation(summary = "Add a room to a hotel", description = "Add a new room to a hotel by providing the required details as a CreateHotelRoomRequestDto JSON object in the request body")
    public ResponseEntity<ResponseWrapper<RoomModel>> addHotelRoom(
            @PathVariable Long hotelId,
            @Valid @RequestBody CreateHotelRoomRequestDto dto,
            @Nullable HttpServletRequest request
    ) {
        if (roomService.getRoomByHotelIdAndRoomNumber(hotelId, dto.getNumber()) != null) {
            throw new ResourceAlreadyExistsException("Room " + dto.getNumber() + " already exists in hotel with id: " + hotelId);
        }
        RoomModel roomModel = roomModelAssembler.toModel(
                roomService.createRoom(dto, hotelId)
        );
        return ResponseEntity.created(
                roomModel.getRequiredLink("self").toUri()
        ).body(ResponseWrapper.getCreatedResponseWrapper(
                roomModel,
                request
        ));
    }

    @GetMapping("/hotels/{hotelId}/rooms/{roomId}")
    @Operation(summary = "Get room details", description = "Get details of a room by providing the hotel id and room id")
    public ResponseEntity<ResponseWrapper<RoomModel>> getHotelRoomDetails(
            @PathVariable Long hotelId,
            @PathVariable Long roomId,
            @Nullable HttpServletRequest request
    ) {
        if(isNotValidHotelRoom(hotelId, roomId)) {
            throw new ResourceNotFoundException("Room not found with id: " + roomId);
        }
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        roomModelAssembler.toModel(
                                roomService.getRoomById(roomId)
                        ),
                        request
                )
        );
    }

    @GetMapping("/hotels/{hotelId}/rooms/{roomId}/reservations")
    @Operation(summary = "Get room reservations", description = "Get all reservations of a room by providing the hotel id and room id")
    public ResponseEntity<ResponseWrapper<PagedResponse<ReservationModel>>> getHotelRoomReservations(
            @PathVariable Long hotelId,
            @PathVariable Long roomId,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_RESERVATION_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIR) String sortDir,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue,
            @Nullable HttpServletRequest request
    ) {
        if (isNotValidHotelRoom(hotelId, roomId)) {
            throw new ResourceNotFoundException("Room not found with id: " + roomId);
        }
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapperPaged(
                        reservationService.getReservationsByRoomId(
                                roomId,
                                getPageable(page, size, sortBy, sortDir),
                                filterBy,
                                filterValue
                        ).map(reservationModelAssembler::toModel),
                        request
                )
        );
    }

    @GetMapping("/hotels/{hotelId}/rooms/{roomId}/reservations/{reservationId}")
    @Operation(summary = "Get room reservation details", description = "Get details of a reservation of a room by providing the hotel id, room id, and reservation id")
    public ResponseEntity<ResponseWrapper<ReservationModel>> getHotelRoomReservationDetails(
            @PathVariable Long hotelId,
            @PathVariable Long roomId,
            @PathVariable Long reservationId,
            @Nullable HttpServletRequest request
    ) {
        if (isNotValidHotelRoomReservation(hotelId, roomId, reservationId)) {
            throw new ResourceNotFoundException("Reservation not found with id: " + reservationId);
        }
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        reservationModelAssembler.toModel(
                                reservationService.getReservationById(reservationId)
                        ),
                        request
                )
        );
    }

    @PatchMapping("/hotels/{hotelId}/rooms/{roomId}/reservations/{reservationId}/status")
    @Operation(summary = "Update room reservation status", description = "Update the status of a reservation of a room by providing the hotel id, room id, reservation id, and the new status")
    public ResponseEntity<ResponseWrapper<ReservationModel>> updateHotelRoomReservationStatus(
            @PathVariable Long hotelId,
            @PathVariable Long roomId,
            @RequestParam String status,
            @PathVariable Long reservationId,
            @Nullable HttpServletRequest request
    ) {
        if (isNotValidHotelRoomReservation(hotelId, roomId, reservationId)) {
            throw new ResourceNotFoundException("Reservation not found with id: " + reservationId);
        }
        try {
            ReservationStatus reservationStatus = ReservationStatus.valueOf(status.toUpperCase());
            return ResponseEntity.ok(
                    ResponseWrapper.getOkResponseWrapper(
                            reservationModelAssembler.toModel(
                                    reservationService.updateReservationStatus(reservationId, reservationStatus)
                            ),
                            request
                    )
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid reservation status: " + status);
        }
    }

    @PutMapping("/hotels/{hotelId}")
    @Operation(summary = "Update hotel details", description = "Update the details of a hotel by providing the hotel id and the required details as an UpdateHotelRequestDto JSON object in the request body")
    public ResponseEntity<ResponseWrapper<HotelModel>> updateHotelDetails(
            @PathVariable Long hotelId,
            @Valid @RequestBody UpdateHotelRequestDto dto,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        hotelModelAssembler.toModel(
                                hotelService.updateHotel(hotelId, dto, true)
                        ),
                        request
                )
        );
    }

    @PatchMapping("/hotels/{hotelId}")
    @Operation(summary = "Update hotel", description = "Update the details of a hotel by providing the hotel id and the required details as an UpdateHotelRequestDto JSON object in the request body")
    public ResponseEntity<ResponseWrapper<HotelModel>> updateHotel(
            @PathVariable Long hotelId,
            @Valid @RequestBody UpdateHotelRequestDto dto,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        hotelModelAssembler.toModel(
                                hotelService.updateHotel(hotelId, dto, false)
                        ),
                        request
                )
        );
    }

    @PutMapping("/hotels/{hotelId}/rooms/{roomId}")
    @Operation(summary = "Update room details", description = "Update the details of a room by providing the hotel id, room id, and the required details as an UpdateHotelRoomRequestDto JSON object in the request body")
    public ResponseEntity<ResponseWrapper<RoomModel>> updateHotelRoomDetails(
            @PathVariable Long hotelId, @PathVariable Long roomId,
            @Valid @RequestBody UpdateHotelRoomRequestDto dto,
            @Nullable HttpServletRequest request
    ) {
        if (isNotValidHotelRoom(hotelId, roomId)) {
            throw new ResourceNotFoundException("Room not found with id: " + roomId);
        }
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        roomModelAssembler.toModel(
                                roomService.updateRoom(roomId, dto, true)
                        ),
                        request
                )
        );
    }

    @PatchMapping("/hotels/{hotelId}/rooms/{roomId}")
    @Operation(summary = "Update room", description = "Update the details of a room by providing the hotel id, room id, and the required details as an UpdateHotelRoomRequestDto JSON object in the request body")
    public ResponseEntity<ResponseWrapper<RoomModel>> updateHotelRoom(
            @PathVariable Long hotelId, @PathVariable Long roomId,
            @Valid @RequestBody UpdateHotelRoomRequestDto dto,
            @Nullable HttpServletRequest request
    ) {
        if (isNotValidHotelRoom(hotelId, roomId)) {
            throw new ResourceNotFoundException("Room not found with id: " + roomId);
        }
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        roomModelAssembler.toModel(
                                roomService.updateRoom(roomId, dto, false)
                        ),
                        request
                )
        );
    }

    @PatchMapping("/hotels/{hotelId}/rooms/{roomId}/satus")
    @Operation(summary = "Update room status", description = "Update the status of a room by providing the hotel id, room id, and the new status")
    public ResponseEntity<ResponseWrapper<RoomModel>> updateHotelRoom(
            @PathVariable Long hotelId, @PathVariable Long roomId,
            @RequestParam String status,
            @Nullable HttpServletRequest request
    ) {
        if (isNotValidHotelRoom(hotelId, roomId)) {
            throw new ResourceNotFoundException("Room not found with id: " + roomId);
        }
        try {
            RoomStatus roomStatus = RoomStatus.valueOf(status.toUpperCase());
            return ResponseEntity.ok(
                    ResponseWrapper.getOkResponseWrapper(
                            roomModelAssembler.toModel(
                                    roomService.updateRoomStatus(roomId, roomStatus)
                            ),
                            request
                    )
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid room status: " + status);
        }
    }

    @DeleteMapping("/hotels/{hotelId}/rooms/{roomId}")
    @Operation(summary = "Delete room", description = "Delete a room by providing the hotel id and room id")
    public ResponseEntity<ResponseWrapper<Void>> deleteHotelRoom(
            @PathVariable Long hotelId,
            @PathVariable Long roomId,
            @Nullable HttpServletRequest request
    ) {
        if (isNotValidHotelRoom(hotelId, roomId)) {
            throw new ResourceNotFoundException("Room not found with id: " + roomId);
        }
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok(
                ResponseWrapper.getNoContentResponseWrapper(
                        request
                )
        );
    }

    private boolean isNotValidHotelRoom(Long hotelId, Long roomId) {
        return !roomService.getRoomById(roomId).getHotelId().equals(hotelId);
    }
    private boolean isNotValidHotelRoomReservation(Long hotelId, Long roomId, Long reservationId) {
        return !roomService.getRoomById(roomId).getHotelId().equals(hotelId) ||
                !reservationService.getReservationById(reservationId).getRoomId().equals(roomId);
    }
}