package com.akm.hotelmanagement.controller.base;

import com.akm.hotelmanagement.assembler.AmenityModelAssembler;
import com.akm.hotelmanagement.assembler.HotelModelAssembler;
import com.akm.hotelmanagement.assembler.ReservationModelAssembler;
import com.akm.hotelmanagement.assembler.RoomModelAssembler;
import com.akm.hotelmanagement.assembler.models.AmenityModel;
import com.akm.hotelmanagement.assembler.models.HotelModel;
import com.akm.hotelmanagement.assembler.models.ReservationModel;
import com.akm.hotelmanagement.dto.amenity.CreateAmenityRequestDto;
import com.akm.hotelmanagement.dto.amenity.UpdateAmenityRequestDto;
import com.akm.hotelmanagement.dto.hotel.UpdateHotelRequestDto;
import com.akm.hotelmanagement.service.AmenityService;
import com.akm.hotelmanagement.service.HotelService;
import com.akm.hotelmanagement.service.ReservationService;
import com.akm.hotelmanagement.service.RoomService;
import com.akm.hotelmanagement.wrapper.PagedResponse;
import com.akm.hotelmanagement.wrapper.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import static com.akm.hotelmanagement.util.Constants.*;
import static com.akm.hotelmanagement.util.Utils.getPageable;

public class AdminBaseController extends BaseController {
    protected final ReservationService reservationService;
    protected final ReservationModelAssembler reservationModelAssembler;

    public AdminBaseController(AmenityService amenityService, AmenityModelAssembler amenityModelAssembler, HotelService hotelService, HotelModelAssembler hotelModelAssembler, RoomService roomService, RoomModelAssembler roomModelAssembler, ReservationService reservationService, ReservationModelAssembler reservationModelAssembler) {
        super(amenityService, amenityModelAssembler, hotelService, hotelModelAssembler, roomService, roomModelAssembler);
        this.reservationService = reservationService;
        this.reservationModelAssembler = reservationModelAssembler;
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

    @GetMapping("/reservations")
    @Operation(summary = "Get all reservations", description = "Get all reservations with pagination, sorting, and filtering options from the database")
    public ResponseEntity<ResponseWrapper<PagedResponse<ReservationModel>>> getAllReservations(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_RESERVATION_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIR) String sortDir,
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
}