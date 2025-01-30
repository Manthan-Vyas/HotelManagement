package com.akm.hotelmanagement.controller;

import com.akm.hotelmanagement.assembler.HotelModelAssembler;
import com.akm.hotelmanagement.assembler.RoomModelAssembler;
import com.akm.hotelmanagement.assembler.UserModelAssembler;
import com.akm.hotelmanagement.assembler.models.HotelModel;
import com.akm.hotelmanagement.assembler.models.RoomModel;
import com.akm.hotelmanagement.assembler.models.UserModel;
import com.akm.hotelmanagement.dto.user.CreateUserRequestDto;
import com.akm.hotelmanagement.service.HotelService;
import com.akm.hotelmanagement.service.RoomService;
import com.akm.hotelmanagement.service.UserService;
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
@RequestMapping("/")
@RequiredArgsConstructor
@Tag(name = "Public Controller", description = "Endpoints accessible to the public")
public class PublicController {

    private final UserService userService;
    private final HotelService hotelService;
    private final RoomService roomService;

    private final UserModelAssembler userModelAssembler;
    private final HotelModelAssembler hotelModelAssembler;
    private final RoomModelAssembler roomModelAssembler;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Register a new user with the provided details")
    public ResponseEntity<ResponseWrapper<UserModel>> registerUser(
            @Valid @RequestBody CreateUserRequestDto userDto,
            @Nullable HttpServletRequest request
    ) {
        UserModel userModel = userModelAssembler.toModel(
                userService.createUser(userDto)
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
                ResponseWrapper.getOkResponseWrapperPaged(
                        responseData,
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
                ResponseWrapper.getOkResponseWrapperPaged(
                        responseData,
                        request
                )
        );
    }
}
