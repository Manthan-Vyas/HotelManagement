package com.akm.hotelmanagement.controller;

import com.akm.hotelmanagement.assembler.*;
import com.akm.hotelmanagement.assembler.models.HotelModel;
import com.akm.hotelmanagement.assembler.models.ReservationModel;
import com.akm.hotelmanagement.assembler.models.RoomModel;
import com.akm.hotelmanagement.assembler.models.UserModel;
import com.akm.hotelmanagement.controller.base.BaseController;
import com.akm.hotelmanagement.dto.reservation.CreateOrUpdateUserRoomReservationRequestDto;
import com.akm.hotelmanagement.dto.user.UpdateUserRequestDto;
import com.akm.hotelmanagement.dto.user.UserResponseDto;
import com.akm.hotelmanagement.service.*;
import com.akm.hotelmanagement.wrapper.PagedResponse;
import com.akm.hotelmanagement.wrapper.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.akm.hotelmanagement.util.Constants.*;
import static com.akm.hotelmanagement.util.Utils.getPageable;
import static com.akm.hotelmanagement.util.Utils.isAuthenticatedUser;

@Validated
@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "Endpoints for user operations")
public class UserController extends BaseController {
    private final UserService userService;
    private final ReservationService reservationService;
    private final UserModelAssembler userModelAssembler;
    private final ReservationModelAssembler reservationModelAssembler;

    public UserController(AmenityService amenityService, AmenityModelAssembler amenityModelAssembler, HotelService hotelService, HotelModelAssembler hotelModelAssembler, RoomService roomService, RoomModelAssembler roomModelAssembler, UserService userService, ReservationService reservationService, UserModelAssembler userModelAssembler, ReservationModelAssembler reservationModelAssembler) {
        super(amenityService, amenityModelAssembler, hotelService, hotelModelAssembler, roomService, roomModelAssembler);
        this.userService = userService;
        this.reservationService = reservationService;
        this.userModelAssembler = userModelAssembler;
        this.reservationModelAssembler = reservationModelAssembler;
    }

    @GetMapping
    @Operation(summary = "User home", description = "Get the home page of a user")
    public ResponseEntity<ResponseWrapper<String>> getUserHome() {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        "Welcome to the user home page",
                        null
                )
        );
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get user details", description = "Get details of a user by providing the username")
    public ResponseEntity<ResponseWrapper<UserModel>> getUserDetails(
            @PathVariable String username,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        userModelAssembler.toModel(getValidUser(username)),
                        request
                )
        );
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update user details", description = "Update details of a user by providing the username and the required details as an UpdateUserRequestDto JSON object in the request body")
    public ResponseEntity<ResponseWrapper<UserModel>> updateUserDetails(
            @PathVariable String username,
            @Valid @RequestBody UpdateUserRequestDto dto,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        userModelAssembler.toModel(
                                userService.updateUser(getValidUser(username).getUsername(), dto, true)
                        ),
                        request
                )
        );
    }

    @PatchMapping("/{username}")
    @Operation(summary = "Update user", description = "Update details of a user by providing the username and the required details as an UpdateUserRequestDto JSON object in the request body")
    public ResponseEntity<ResponseWrapper<UserModel>> updateUser(
            @PathVariable String username,
            @Valid @RequestBody UpdateUserRequestDto dto,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        userModelAssembler.toModel(
                                userService.updateUser(getValidUser(username).getUsername(), dto, false)
                        ),
                        request
                )
        );
    }

    @PostMapping("/{username}/reservations/{roomId}")
    @Operation(summary = "Book a room", description = "Book a room by providing the username, room ID, and the required details as a CreateOrUpdateUserRoomReservationRequestDto JSON object in the request body")
    public ResponseEntity<ResponseWrapper<ReservationModel>> bookReservation(
            @PathVariable String username,
            @PathVariable Long roomId,
            @Valid @RequestBody CreateOrUpdateUserRoomReservationRequestDto dto,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        reservationModelAssembler.toModel(
                                reservationService.createReservation(
                                        dto,
                                        getValidUser(username).getUsername(),
                                        roomId
                                )
                        ),
                        request
                )
        );
    }

    @GetMapping("/{username}/reservations")
    @Operation(summary = "Get user reservations", description = "Get all reservations of a user by providing the username with pagination, sorting, and filtering options")
    public ResponseEntity<ResponseWrapper<PagedResponse<ReservationModel>>> getUserReservations(
            @PathVariable String username,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_RESERVATION_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIR) String sortDir,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapperPaged(
                        reservationService.getReservationsByUsername(
                                getValidUser(username).getUsername(),
                                getPageable(page, size, sortBy, sortDir),
                                filterBy,
                                filterValue
                        ).map(reservationModelAssembler::toModel),
                        request
                )
        );
    }

    @GetMapping("/{username}/reservations/{reservationId}")
    @Operation(summary = "Get user reservation", description = "Get a reservation of a user by providing the username and reservation ID")
    public ResponseEntity<ResponseWrapper<ReservationModel>> getUserReservation(
            @PathVariable String username,
            @PathVariable Long reservationId,
            @Nullable HttpServletRequest request
    ) {
        if (isNotValidUserReservation(username, reservationId)) {
            throw new AccessDeniedException("Access denied: You can only access your own data");
        }
        ReservationModel reservationModel = reservationModelAssembler.toModel(
                reservationService.getReservationById(reservationId)
        );
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        reservationModel,
                        request
                )
        );
    }

    @GetMapping("/{username}/reservations/{reservationId}/hotel")
    @Operation(summary = "Get hotel by reservation ID", description = "Get the hotel details of a reservation by providing the reservation ID")
    public ResponseEntity<ResponseWrapper<HotelModel>> getHotelByReservationId(
            @PathVariable String username,
            @PathVariable Long reservationId,
            @Nullable HttpServletRequest request
    ) {
        if (isNotValidUserReservation(username, reservationId)) {
            throw new AccessDeniedException("Access denied: You can only access your own data");
        }
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        hotelModelAssembler.toModel(
                                hotelService.getHotelByReservationId(reservationId)
                        ),
                        request
                )
        );
    }

    @GetMapping("/{username}/reservations/{reservationId}/room")
    @Operation(summary = "Get room by reservation ID", description = "Get the room details of a reservation by providing the reservation ID")
    public ResponseEntity<ResponseWrapper<RoomModel>> getRoomByReservationId(
            @PathVariable String username,
            @PathVariable Long reservationId,
            @Nullable HttpServletRequest request
    ) {
        if (isNotValidUserReservation(username, reservationId)) {
            throw new AccessDeniedException("Access denied: You can only access your own data");
        }
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        roomModelAssembler.toModel(
                                roomService.getRoomByReservationId(reservationId)
                        ),
                        request
                )
        );
    }

    @PutMapping("/{username}/reservations/{reservationId}")
    @Operation(summary = "Update user reservation", description = "Update a reservation of a user by providing the username, reservation ID, and the required details as a CreateOrUpdateUserRoomReservationRequestDto JSON object in the request body")
    public ResponseEntity<ResponseWrapper<ReservationModel>> updateReservation(
            @PathVariable String username,
            @PathVariable Long reservationId,
            @Valid @RequestBody CreateOrUpdateUserRoomReservationRequestDto dto,
            @Nullable HttpServletRequest request
    ) {
        if (isNotValidUserReservation(username, reservationId)) {
            throw new AccessDeniedException("Access denied: You can only access your own data");
        }
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        reservationModelAssembler.toModel(
                                reservationService.updateReservation(reservationId, dto)
                        ),
                        request
                )
        );
    }

    @PatchMapping("/{username}/reservations/{reservationId}/cancel")
    @Operation(summary = "Cancel user reservation", description = "Cancel a reservation of a user by providing the username and reservation ID")
    public ResponseEntity<ResponseWrapper<ReservationModel>> cancelReservation(
            @PathVariable String username,
            @PathVariable Long reservationId,
            @Nullable HttpServletRequest request
    ) {
        if (isNotValidUserReservation(username, reservationId)) {
            throw new AccessDeniedException("Access denied: You can only access your own data");
        }
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        reservationModelAssembler.toModel(
                                reservationService.cancelReservation(reservationId)
                        ),
                        request
                )
        );
    }

    private UserResponseDto getValidUser(String username) {
        if (!isAuthenticatedUser(username)) {
            throw new AccessDeniedException("Access denied: You can only access your own data");
        } else {
            return userService.getUserByUsername(username);
        }
    }

    private boolean isNotValidUserReservation(String username, Long reservationId) {
        return !isAuthenticatedUser(username) || !reservationService.getReservationById(reservationId).getUsername().equals(username);
    }
}