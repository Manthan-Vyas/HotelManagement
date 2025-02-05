package com.akm.hotelmanagement.controller;

import com.akm.hotelmanagement.assembler.*;
import com.akm.hotelmanagement.assembler.models.HotelModel;
import com.akm.hotelmanagement.assembler.models.ReservationModel;
import com.akm.hotelmanagement.assembler.models.RoomModel;
import com.akm.hotelmanagement.assembler.models.UserModel;
import com.akm.hotelmanagement.controller.base.AdminBaseController;
import com.akm.hotelmanagement.dto.hotel.CreateHotelRequestDto;
import com.akm.hotelmanagement.dto.user.CreateUserRequestDto;
import com.akm.hotelmanagement.dto.user.UpdateUserRequestDto;
import com.akm.hotelmanagement.dto.user.UserResponseDto;
import com.akm.hotelmanagement.exception.ResourceAlreadyExistsException;
import com.akm.hotelmanagement.service.*;
import com.akm.hotelmanagement.wrapper.PagedResponse;
import com.akm.hotelmanagement.wrapper.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.akm.hotelmanagement.util.Constants.*;
import static com.akm.hotelmanagement.util.Utils.getPageable;

@Validated
@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Admin operations")
public class AdminController extends AdminBaseController {
    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    public AdminController(AmenityService amenityService, AmenityModelAssembler amenityModelAssembler, HotelService hotelService, HotelModelAssembler hotelModelAssembler, RoomService roomService, RoomModelAssembler roomModelAssembler, ReservationService reservationService, ReservationModelAssembler reservationModelAssembler, UserService userService, UserModelAssembler userModelAssembler) {
        super(amenityService, amenityModelAssembler, hotelService, hotelModelAssembler, roomService, roomModelAssembler, reservationService, reservationModelAssembler);
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
    }

    @GetMapping
    @Operation(summary = "Admin home", description = "Get the admin home page")
    public ResponseEntity<ResponseWrapper<String>> getAdminHome() {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        "Welcome to the admin home page",
                        null
                )
        );
    }

    @PostMapping("/users")
    @Operation(summary = "Create user", description = "Create a new user by providing the required details as a CreateUserRequestDto JSON object in the request body")
    public ResponseEntity<ResponseWrapper<UserModel>> createUser(
            @Valid @RequestBody CreateUserRequestDto dto,
            @Nullable HttpServletRequest request
    ) {
        if (userService.existsByUsername(dto.getUsername())) {
            throw new ResourceAlreadyExistsException("User with username " + dto.getUsername() + " already exists");
        }
        if (userService.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException("User with email " + dto.getEmail() + " already exists");
        }
        UserModel userModel = userModelAssembler.toModel(
                userService.createUser(dto)
        );
        return ResponseEntity.created(
                userModel.getRequiredLink("self").toUri()
        ).body(
                ResponseWrapper.getCreatedResponseWrapper(
                        userModel,
                        request
                )
        );
    }

    @PostMapping("/hotels")
    @Operation(summary = "Create hotel", description = "Create a new hotel by providing the required details as a CreateHotelRequestDto JSON object in the request body")
    public ResponseEntity<ResponseWrapper<HotelModel>> createHotel(
            @Valid @RequestBody CreateHotelRequestDto dto,
            @Nullable HttpServletRequest request
    ) {
        HotelModel hotelModel = hotelModelAssembler.toModel(
                hotelService.createHotel(dto)
        );
        return ResponseEntity.created(
                hotelModel.getRequiredLink("self").toUri()
        ).body(
                ResponseWrapper.getCreatedResponseWrapper(
                        hotelModel,
                        request
                )
        );
    }

    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Get all users with pagination, sorting, and filtering options from the database")
    public ResponseEntity<ResponseWrapper<PagedResponse<UserModel>>> getAllUsers(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_USER_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIR) String sortDir,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue,
            @Nullable HttpServletRequest request
    ) {
        Page<UserResponseDto> responseData = userService.getAllUsers(
                getPageable(page, size, sortBy, sortDir),
                filterBy,
                filterValue
        );
        if (responseData == null ||responseData.isEmpty()) {
            return ResponseEntity.ok(
                    ResponseWrapper.getNoContentResponseWrapper(request)
            );
        }
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapperPaged(
                        responseData.map(userModelAssembler::toModel),
                        request
                )
        );
    }

    @GetMapping("/users/{username}")
    @Operation(summary = "Get user details", description = "Get the details of a user by providing the username in the path")
    public ResponseEntity<ResponseWrapper<UserModel>> getUserDetails(
            @PathVariable String username,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        userModelAssembler.toModel(userService.getUserByUsername(username)),
                        request
                )
        );
    }

    @GetMapping("reservations/{reservationId}")
    @Operation(summary = "Get reservation details", description = "Get details of a reservation by providing the reservation id")
    public ResponseEntity<ResponseWrapper<ReservationModel>> getReservationDetails(
            @PathVariable Long reservationId,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        reservationModelAssembler.toModel(
                                reservationService.getReservationById(reservationId)
                        ),
                        request
                )
        );
    }

    @GetMapping("reservations/{reservationId}/room")
    @Operation(summary = "Get room by reservation ID", description = "Get the room details by providing the reservation ID in the path")
    public ResponseEntity<ResponseWrapper<RoomModel>> getRoomByReservationId(
            @PathVariable Long reservationId, HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        roomModelAssembler.toModel(
                                roomService.getRoomByReservationId(reservationId)
                        ),
                        request
                )
        );
    }

    @GetMapping("reservations/{reservationId}/hotel")
    @Operation(summary = "Get hotel by reservation ID", description = "Get the hotel details by providing the reservation ID in the path")
    public ResponseEntity<ResponseWrapper<HotelModel>> getHotelByReservationId(
            @PathVariable Long reservationId, HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        hotelModelAssembler.toModel(
                                hotelService.getHotelByReservationId(reservationId)
                        ),
                        request
                )
        );
    }

    @GetMapping("reservations/{reservationId}/user")
    @Operation(summary = "Get user by reservation ID", description = "Get the user details by providing the reservation ID in the path")
    public ResponseEntity<ResponseWrapper<UserModel>> getUserByReservationId(
            @PathVariable Long reservationId, HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        userModelAssembler.toModel(
                                userService.getUserByReservationId(reservationId)
                        ),
                        request
                )
        );
    }

    @PutMapping("/users/{username}")
    @Operation(summary = "Update user details", description = "Update the details of a user by providing the username in the path and the required details as an UpdateUserRequestDto JSON object in the request body")
    public ResponseEntity<ResponseWrapper<UserModel>> updateUserDetails(
            @PathVariable String username,
            @Valid @RequestBody UpdateUserRequestDto dto,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        userModelAssembler.toModel(
                                userService.updateUser(username, dto, true)
                        ),
                        request
                )
        );
    }

    @PatchMapping("/users/{username}")
    @Operation(summary = "Update user", description = "Update the details of a user by providing the username in the path and the required details as an UpdateUserRequestDto JSON object in the request body")
    public ResponseEntity<ResponseWrapper<UserModel>> updateUser(
            @PathVariable String username,
            @Valid @RequestBody UpdateUserRequestDto dto,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        userModelAssembler.toModel(
                                userService.updateUser(username, dto, false)
                        ),
                        request
                )
        );
    }

    @PatchMapping("/users/{username}/enabled")
    @Operation(summary = "Change user enabled status", description = "Change the enabled status of a user by providing the username in the path and the enabled status as a query parameter")
    public ResponseEntity<ResponseWrapper<UserModel>> changeUserEnabled(
            @PathVariable String username,
            @RequestParam boolean enabled,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        userModelAssembler.toModel(
                                userService.changeUserEnabled(username, enabled)
                        ),
                        request
                )
        );
    }

    @DeleteMapping("/users/{username}")
    @Operation(summary = "Delete user", description = "Delete a user by providing the username in the path")
    public ResponseEntity<ResponseWrapper<UserModel>> deleteUser(
            @PathVariable String username,
            @Nullable HttpServletRequest request
    ) {
        userService.deleteUser(username);
        return ResponseEntity.ok(
                ResponseWrapper.getNoContentResponseWrapper(
                        request
                )
        );
    }

    @DeleteMapping("/hotels/{hotelId}")
    @Operation(summary = "Delete hotel", description = "Delete a hotel by providing the hotel ID in the path")
    public ResponseEntity<ResponseWrapper<HotelModel>> deleteHotel(
            @PathVariable Long hotelId,
            @Nullable HttpServletRequest request
    ) {
        hotelService.deleteHotel(hotelId);
        return ResponseEntity.ok(
                ResponseWrapper.getNoContentResponseWrapper(
                        request
                )
        );
    }

    @DeleteMapping("/amenities/{amenityId}")
    @Operation(summary = "Delete amenity", description = "Delete an amenity by providing the amenity ID in the path")
    public ResponseEntity<ResponseWrapper<Void>> deleteAmenity(
            @PathVariable Long amenityId,
            @Nullable HttpServletRequest request
    ) {
        amenityService.deleteAmenity(amenityId);
        return ResponseEntity.ok(
                ResponseWrapper.getNoContentResponseWrapper(
                        request
                )
        );
    }
}