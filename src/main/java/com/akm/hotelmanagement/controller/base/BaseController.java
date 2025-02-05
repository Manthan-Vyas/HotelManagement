package com.akm.hotelmanagement.controller.base;

import com.akm.hotelmanagement.assembler.AmenityModelAssembler;
import com.akm.hotelmanagement.assembler.HotelModelAssembler;
import com.akm.hotelmanagement.assembler.RoomModelAssembler;
import com.akm.hotelmanagement.assembler.models.AmenityModel;
import com.akm.hotelmanagement.assembler.models.HotelModel;
import com.akm.hotelmanagement.assembler.models.RoomModel;
import com.akm.hotelmanagement.service.AmenityService;
import com.akm.hotelmanagement.service.HotelService;
import com.akm.hotelmanagement.service.RoomService;
import com.akm.hotelmanagement.wrapper.PagedResponse;
import com.akm.hotelmanagement.wrapper.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

import static com.akm.hotelmanagement.util.Constants.*;
import static com.akm.hotelmanagement.util.Utils.getPageable;

@AllArgsConstructor
public abstract class BaseController {
    protected final AmenityService amenityService;
    protected final AmenityModelAssembler amenityModelAssembler;
    protected final HotelService hotelService;
    protected final HotelModelAssembler hotelModelAssembler;
    protected final RoomService roomService;
    protected final RoomModelAssembler roomModelAssembler;

    @GetMapping("/amenities")
    @Operation(summary = "Get all amenities", description = "Get all amenities with pagination, sorting, and filtering options from the database")
    public ResponseEntity<ResponseWrapper<PagedResponse<AmenityModel>>> getAllAmenities(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_AMENITY_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIR) String sortDir,
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

    @GetMapping("/hotels/{hotelId}/amenities")
    @Operation(summary = "Get hotel's amenities", description = "Get all amenities of a hotel by providing the hotel ID in the path")
    public ResponseEntity<ResponseWrapper<PagedResponse<AmenityModel>>> getAmenitiesByHotelId(
            @PathVariable Long hotelId,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_AMENITY_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIR) String sortDir,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapperPaged(
                        amenityService.getAmenitiesByHotelId(
                                hotelId,
                                getPageable(page, size, sortBy, sortDir),
                                filterBy,
                                filterValue
                        ).map(amenityModelAssembler::toModel),
                        request
                )
        );
    }

    @GetMapping("/rooms/{roomId}/amenities")
    @Operation(summary = "Get room's Hotel's amenities", description = "Get all amenities of a hotel that contains room by providing the room ID in the path")
    public ResponseEntity<ResponseWrapper<PagedResponse<AmenityModel>>> getAmenitiesByRoomId(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_AMENITY_SORT_BY) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapperPaged(
                        amenityService.getAmenitiesByRoomId(
                                roomId,
                                getPageable(page, size, sortBy, sortDir),
                                filterBy,
                                filterValue
                        ).map(amenityModelAssembler::toModel),
                        request
                )
        );
    }

    @GetMapping("/hotels")
    @Operation(summary = "Get all hotels", description = "Get all hotels with pagination, sorting, and filtering options from the database")
    public ResponseEntity<ResponseWrapper<PagedResponse<HotelModel>>> getAllHotels(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_HOTEL_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIR) String sortDir,
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

    @GetMapping("/amenities/{amenityId}/hotels")
    @Operation(summary = "Get Hotels having Amenity", description = "Get all hotels with a specific amenity by providing the amenity ID in the path")
    public ResponseEntity<ResponseWrapper<PagedResponse<HotelModel>>> getHotelsByAmenityId(
            @PathVariable Long amenityId,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_HOTEL_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIR) String sortDir,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue,
            @Nullable HttpServletRequest request
    ) {
        Page<HotelModel> responseData = hotelService.getHotelsByAmenityId(
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

    @GetMapping("/rooms")
    @Operation(summary = "Get all rooms", description = "Get all rooms with pagination, sorting, and filtering options from the database")
    public ResponseEntity<ResponseWrapper<PagedResponse<RoomModel>>> getAllRooms(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_ROOM_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIR) String sortDir,
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

    @GetMapping("/hotels/{hotelId}/rooms")
    @Operation(summary = "Get hotel rooms", description = "Get all rooms of a hotel by providing the hotel id")
    public ResponseEntity<ResponseWrapper<PagedResponse<RoomModel>>> getRoomsByHotelId(
            @PathVariable Long hotelId,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_HOTEL_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIR) String sortDir,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapperPaged(
                        roomService.getRoomsByHotelId(
                                hotelId,
                                getPageable(page, size, sortBy, sortDir),
                                filterBy,
                                filterValue
                        ).map(roomModelAssembler::toModel),
                        request
                )
        );
    }

    @GetMapping("/amenities/{amenityId}/rooms")
    @Operation(summary = "Get amenity rooms", description = "Get all rooms with a specific amenity by providing the amenity ID in the path")
    public ResponseEntity<ResponseWrapper<PagedResponse<RoomModel>>> getRoomsByAmenityId(
            @PathVariable Long amenityId,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_AMENITY_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIR) String sortDir,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue,
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapperPaged(
                        roomService.getRoomsByAmenityId(
                                amenityId,
                                getPageable(page, size, sortBy, sortDir),
                                filterBy,
                                filterValue
                        ).map(roomModelAssembler::toModel),
                        request
                )
        );
    }

    @GetMapping("/rooms/available")
    @Operation(summary = "Get available rooms", description = "Get available rooms based on check-in, check-out dates, and number of guests with pagination, sorting, and filtering options")
    public ResponseEntity<ResponseWrapper<PagedResponse<RoomModel>>> getAvailableRooms(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut,
            @RequestParam int noOfGuests,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_ROOM_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIR) String sortDir,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue,
            @Nullable HttpServletRequest request
    ) {
        Page<RoomModel> responseData = roomService.getAvailableRooms(
                checkIn, checkOut, noOfGuests, getPageable(page, size, sortBy, sortDir), filterBy, filterValue
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
}