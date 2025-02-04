package com.akm.hotelmanagement.controller;

import com.akm.hotelmanagement.assembler.AmenityModelAssembler;
import com.akm.hotelmanagement.assembler.HotelModelAssembler;
import com.akm.hotelmanagement.assembler.RoomModelAssembler;
import com.akm.hotelmanagement.assembler.UserModelAssembler;
import com.akm.hotelmanagement.assembler.models.UserModel;
import com.akm.hotelmanagement.controller.base.BaseController;
import com.akm.hotelmanagement.dto.user.CreateUserRequestDto;
import com.akm.hotelmanagement.service.AmenityService;
import com.akm.hotelmanagement.service.HotelService;
import com.akm.hotelmanagement.service.RoomService;
import com.akm.hotelmanagement.service.UserService;
import com.akm.hotelmanagement.wrapper.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequestMapping("/")
@Tag(name = "Public", description = "Endpoints accessible to the public")
public class PublicController extends BaseController {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;
    private final MessageSource messageSource;

    public PublicController(AmenityService amenityService, AmenityModelAssembler amenityModelAssembler, HotelService hotelService, HotelModelAssembler hotelModelAssembler, RoomService roomService, RoomModelAssembler roomModelAssembler, UserService userService, UserModelAssembler userModelAssembler, MessageSource messageSource) {
        super(amenityService, amenityModelAssembler, hotelService, hotelModelAssembler, roomService, roomModelAssembler);
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
        this.messageSource = messageSource;
    }

    @GetMapping
    @Operation(summary = "Get welcome message", description = "Get a welcome message in the default language")
    public ResponseEntity<ResponseWrapper<String>> getWelcomeMessage(
            @Nullable HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.getOkResponseWrapper(
                        messageSource.getMessage("welcome.message", null, request.getLocale()),
                        request
                )
        );
    }

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
}
