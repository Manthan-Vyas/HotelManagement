package com.akm.hotelmanagement.assembler;

import com.akm.hotelmanagement.assembler.models.ReservationModel;
import com.akm.hotelmanagement.controller.AdminController;
import com.akm.hotelmanagement.dto.reservation.ReservationResponseDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReservationModelAssembler extends RepresentationModelAssemblerSupport<ReservationResponseDto, ReservationModel> {
    public ReservationModelAssembler() {
        super(AdminController.class, ReservationModel.class);
    }

    @Override
    @NonNull
    public ReservationModel toModel(@NonNull ReservationResponseDto dto) {
        ReservationModel reservationModel = new ReservationModel(dto);
        reservationModel.add(
                linkTo(methodOn(AdminController.class).getReservationDetails(dto.getId(), null)).withSelfRel(),
                linkTo(methodOn(AdminController.class).getUserDetails(dto.getUser().getUsername(), null)).withRel("user"),
                linkTo(methodOn(AdminController.class).getRoomDetails(dto.getRoom().getId(), null)).withRel("room")
        );
        return reservationModel;
    }
}