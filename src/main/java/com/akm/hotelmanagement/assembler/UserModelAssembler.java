package com.akm.hotelmanagement.assembler;

import com.akm.hotelmanagement.assembler.models.UserModel;
import com.akm.hotelmanagement.controller.AdminController;
import com.akm.hotelmanagement.controller.UserController;
import com.akm.hotelmanagement.dto.user.UserResponseDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static com.akm.hotelmanagement.util.Constants.*;
import static com.akm.hotelmanagement.util.Utils.getCurrentUserRole;
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
        switch (getCurrentUserRole()) {
            case ADMIN -> userModel.add(
                    linkTo(methodOn(AdminController.class).getUserDetails(user.getUsername(), null)).withSelfRel(),
                    linkTo(methodOn(AdminController.class).getAllUsers(
                            DEFAULT_PAGE_NUMBER_INT,
                            DEFAULT_PAGE_SIZE_INT,
                            DEFAULT_USER_SORT_BY, DEFAULT_SORT_DIR,
                            null, null, null
                    )).withRel("all-users")
            );
            case USER -> userModel.add(
                    linkTo(methodOn(UserController.class).getUserDetails(user.getUsername(), null)).withSelfRel(),
                    linkTo(methodOn(UserController.class).getUserReservations(
                            user.getUsername(),
                            DEFAULT_PAGE_NUMBER_INT,
                            DEFAULT_PAGE_SIZE_INT,
                            DEFAULT_RESERVATION_SORT_BY, DEFAULT_SORT_DIR,
                            null, null, null
                    )).withRel("user-reservations")
            );
        }
        return userModel;
    }
}