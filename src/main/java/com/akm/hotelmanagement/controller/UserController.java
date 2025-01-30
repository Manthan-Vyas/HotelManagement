package com.akm.hotelmanagement.controller;

import com.akm.hotelmanagement.assembler.HotelModelAssembler;
import com.akm.hotelmanagement.assembler.ReservationModelAssembler;
import com.akm.hotelmanagement.assembler.RoomModelAssembler;
import com.akm.hotelmanagement.assembler.UserModelAssembler;
import com.akm.hotelmanagement.assembler.models.HotelModel;
import com.akm.hotelmanagement.assembler.models.ReservationModel;
import com.akm.hotelmanagement.assembler.models.RoomModel;
import com.akm.hotelmanagement.assembler.models.UserModel;
import com.akm.hotelmanagement.dto.reservation.CreateOrUpdateUserRoomReservationRequestDto;
import com.akm.hotelmanagement.dto.user.UpdateUserRequestDto;
import com.akm.hotelmanagement.dto.user.UserResponseDto;
import com.akm.hotelmanagement.service.HotelService;
import com.akm.hotelmanagement.service.ReservationService;
import com.akm.hotelmanagement.service.RoomService;
import com.akm.hotelmanagement.service.UserService;
import com.akm.hotelmanagement.wrapper.PagedResponse;
import com.akm.hotelmanagement.wrapper.ResponseWrapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.akm.hotelmanagement.util.Utils.getPageable;
import static com.akm.hotelmanagement.util.Utils.isAuthenticatedUser;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Endpoints for user operations")
public class UserController {

    private final UserService userService;
    private final HotelService hotelService;
    private final RoomService roomService;
    private final ReservationService reservationService;

    private final UserModelAssembler userModelAssembler;
    private final HotelModelAssembler hotelModelAssembler;
    private final RoomModelAssembler roomModelAssembler;
    private final ReservationModelAssembler reservationModelAssembler;

    @GetMapping("/{username}")
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
    public ResponseEntity<ResponseWrapper<PagedResponse<ReservationModel>>> getUserReservations(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "checkIn") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapperPaged(
                        reservationService.getAllUserReservations(
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

    @PutMapping("/{username}/reservations/{reservationId}")
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

    @PutMapping("/{username}/reservations/{reservationId}/cancel")
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

    @GetMapping("/hotels")
    public ResponseEntity<ResponseWrapper<PagedResponse<HotelModel>>> getAllHotels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapperPaged(
                        hotelService.getAllHotels(
                                getPageable(page, size, sortBy, sortDir),
                                filterBy,
                                filterValue
                        ).map(hotelModelAssembler::toModel),
                        request
                )
        );
    }

    @GetMapping("/rooms")
    public ResponseEntity<ResponseWrapper<PagedResponse<RoomModel>>> getAllRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "number") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapperPaged(
                        roomService.getAllRooms(
                                getPageable(page, size, sortBy, sortDir),
                                filterBy,
                                filterValue
                        ).map(roomModelAssembler::toModel),
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
        return !isAuthenticatedUser(username) || !reservationService.getReservationById(reservationId).getUser().getUsername().equals(username);
    }
}