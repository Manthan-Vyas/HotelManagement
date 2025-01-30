package com.akm.hotelmanagement.controller;

import com.akm.hotelmanagement.assembler.*;
import com.akm.hotelmanagement.assembler.models.*;
import com.akm.hotelmanagement.dto.amenity.CreateAmenityRequestDto;
import com.akm.hotelmanagement.dto.amenity.UpdateAmenityRequestDto;
import com.akm.hotelmanagement.dto.hotel.CreateHotelRequestDto;
import com.akm.hotelmanagement.dto.hotel.UpdateHotelRequestDto;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import static com.akm.hotelmanagement.util.Utils.getPageable;

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

    private final UserModelAssembler userModelAssembler;
    private final HotelModelAssembler hotelModelAssembler;
    private final AmenityModelAssembler amenityModelAssembler;
    private final RoomModelAssembler roomModelAssembler;
    private final ReservationModelAssembler reservationModelAssembler;

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

    @PostMapping("/amenities")
    @Operation(summary = "Create amenity", description = "Create a new amenity by providing the required details as a CreateAmenityRequestDto JSON object in the request body")
    public ResponseEntity<ResponseWrapper<AmenityModel>> createAmenity(
            @Valid @RequestBody CreateAmenityRequestDto dto,
            @Nullable HttpServletRequest request
    ) {
        AmenityModel amenityModel = amenityModelAssembler.toModel(
                amenityService.createAmenity(dto)
        );
        return ResponseEntity.created(
                amenityModel.getRequiredLink("self").toUri()
        ).body(
                ResponseWrapper.getCreatedResponseWrapper(
                        amenityModel,
                        request
                )
        );
    }

    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Get all users with pagination, sorting, and filtering options from the database")
    public ResponseEntity<ResponseWrapper<PagedResponse<UserModel>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "email") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
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

    @GetMapping("/hotels")
    @Operation(summary = "Get all hotels", description = "Get all hotels with pagination, sorting, and filtering options from the database")
    public ResponseEntity<ResponseWrapper<PagedResponse<HotelModel>>> getAllHotels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue,
            @Nullable HttpServletRequest request
    ) {
        Page<HotelModel> responseData = hotelService.getAllHotels(
                getPageable(page, size, sortBy, sortDir),
                filterBy,
                filterValue
        ).map(hotelModelAssembler::toModel);
        if (responseData.isEmpty()) {
            return ResponseEntity.ok(
                    ResponseWrapper.getNoContentResponseWrapper(request)
            );
        }
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapperPaged(responseData, request)
        );
    }

    @GetMapping("/rooms")
    @Operation(summary = "Get all rooms", description = "Get all rooms with pagination, sorting, and filtering options from the database")
    public ResponseEntity<ResponseWrapper<PagedResponse<RoomModel>>> getAllRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue,
            @Nullable HttpServletRequest request
    ) {
        Page<RoomModel> responseData = roomService.getAllRooms(
                getPageable(page, size, sortBy, sortDir),
                filterBy,
                filterValue
        ).map(roomModelAssembler::toModel);
        if (responseData.isEmpty()) {
            return ResponseEntity.ok(
                    ResponseWrapper.getNoContentResponseWrapper(request)
            );
        }
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapperPaged(responseData, request)
        );
    }

    @GetMapping("/amenities")
    @Operation(summary = "Get all amenities", description = "Get all amenities with pagination, sorting, and filtering options from the database")
    public ResponseEntity<ResponseWrapper<PagedResponse<AmenityModel>>> getAllAmenities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue,
            @Nullable HttpServletRequest request
    ) {
        Page<AmenityModel> responseData = amenityService.getAllAmenities(
                getPageable(page, size, sortBy, sortDir),
                filterBy,
                filterValue
        ).map(amenityModelAssembler::toModel);
        if (responseData.isEmpty()) {
            return ResponseEntity.ok(
                    ResponseWrapper.getNoContentResponseWrapper(request)
            );
        }
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapperPaged(responseData, request)
        );
    }

    @GetMapping("/reservations")
    @Operation(summary = "Get all reservations", description = "Get all reservations with pagination, sorting, and filtering options from the database")
    public ResponseEntity<ResponseWrapper<PagedResponse<ReservationModel>>> getAllReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "checkInDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue,
            @Nullable HttpServletRequest request
    ) {
        Page<ReservationModel> responseData = reservationService.getAllReservations(
                getPageable(page, size, sortBy, sortDir),
                filterBy,
                filterValue
        ).map(reservationModelAssembler::toModel);
        if (responseData.isEmpty()) {
            return ResponseEntity.ok(
                    ResponseWrapper.getNoContentResponseWrapper(request)
            );
        }
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapperPaged(responseData, request)
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

    @GetMapping("/hotels/{hotelId}")
    @Operation(summary = "Get hotel details", description = "Get the details of a hotel by providing the hotel ID in the path")
    public ResponseEntity<ResponseWrapper<HotelModel>> getHotelDetails(
            @PathVariable Long hotelId,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                    hotelModelAssembler.toModel(hotelService.getHotelById(hotelId)),
                    request
                )
        );
    }

    @GetMapping("/rooms/{roomId}")
    @Operation(summary = "Get room details", description = "Get the details of a room by providing the room ID in the path")
    public ResponseEntity<ResponseWrapper<RoomModel>> getRoomDetails(
            @PathVariable Long roomId,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        roomModelAssembler.toModel(roomService.getRoomById(roomId)),
                        request
                )
        );
    }

    @GetMapping("/amenities/{amenityId}")
    @Operation(summary = "Get amenity details", description = "Get the details of an amenity by providing the amenity ID in the path")
    public ResponseEntity<ResponseWrapper<AmenityModel>> getAmenityDetails(
            @PathVariable Long amenityId,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        amenityModelAssembler.toModel(amenityService.getAmenityById(amenityId)),
                        request
                )
        );
    }

    @GetMapping("/reservations/{reservationId}")
    @Operation(summary = "Get reservation details", description = "Get the details of a reservation by providing the reservation ID in the path")
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

    @PutMapping("/hotels/{hotelId}")
    @Operation(summary = "Update hotel details", description = "Update the details of a hotel by providing the hotel ID in the path and the required details as an UpdateHotelRequestDto JSON object in the request body")
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
    @Operation(summary = "Update hotel", description = "Update the details of a hotel by providing the hotel ID in the path and the required details as an UpdateHotelRequestDto JSON object in the request body")
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

    @PutMapping("/amenities/{amenityId}")
    @Operation(summary = "Update amenity details", description = "Update the details of an amenity by providing the amenity ID in the path and the required details as an UpdateAmenityRequestDto JSON object in the request body")
    public ResponseEntity<ResponseWrapper<AmenityModel>> updateAmenityDetails(
            @PathVariable Long amenityId,
            @Valid @RequestBody UpdateAmenityRequestDto dto,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        amenityModelAssembler.toModel(
                                amenityService.updateAmenity(amenityId, dto, true)
                        ),
                        request
                )
        );
    }

    @PatchMapping("/amenities/{amenityId}")
    @Operation(summary = "Update amenity", description = "Update the details of an amenity by providing the amenity ID in the path and the required details as an UpdateAmenityRequestDto JSON object in the request body")
    public ResponseEntity<ResponseWrapper<AmenityModel>> updateAmenity(
            @PathVariable Long amenityId,
            @Valid @RequestBody UpdateAmenityRequestDto dto,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        amenityModelAssembler.toModel(
                                amenityService.updateAmenity(amenityId, dto, false)
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

    @GetMapping("/amenities/{amenityId}/hotels")
    @Operation(summary = "Get amenity hotels", description = "Get all hotels with a specific amenity by providing the amenity ID in the path")
    public ResponseEntity<ResponseWrapper<PagedResponse<HotelModel>>> getAmenityHotels(
            @PathVariable Long amenityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue,
            @Nullable HttpServletRequest request
    ) {
        Page<HotelModel> responseData = hotelService.getAmenityHotels(
                amenityId,
                getPageable(page, size, sortBy, sortDir),
                filterBy,
                filterValue
        ).map(hotelModelAssembler::toModel);
        if (responseData.isEmpty()) {
            return ResponseEntity.ok(
                    ResponseWrapper.getNoContentResponseWrapper(request)
            );
        }
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapperPaged(responseData, request)
        );
    }
}