package com.akm.hotelmanagement.assembler;

import com.akm.hotelmanagement.assembler.models.UserModel;
import com.akm.hotelmanagement.controller.UserController;
import com.akm.hotelmanagement.dto.user.UserResponseDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<UserResponseDto, UserModel> {
    public UserModelAssembler() {
        super(UserController.class, UserModel.class);
    }

    @Override
    public @NonNull UserModel toModel(@NonNull UserResponseDto user) {
        UserModel userModel = new UserModel(user);
        userModel.add(
                linkTo(methodOn(UserController.class).getUserDetails(user.getUsername(), null)).withSelfRel(),
                linkTo(methodOn(UserController.class).getUserReservations(user.getUsername(), 0, 10, "reservationDate", "asc", null, null, null)).withRel("reservations") //todo: desc and reservationDate or reservation-date?
        );
        return userModel;
    }
}