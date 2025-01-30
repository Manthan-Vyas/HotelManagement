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
    public ResponseEntity<ResponseWrapper<Void>> getAdminHome() {
        return ResponseEntity.ok(
                ResponseWrapper.getNoContentResponseWrapper(
                        null
                )
        );
    }

    @PostMapping("/users")
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
        if (responseData.isEmpty()) {
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