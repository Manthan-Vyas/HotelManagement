package com.akm.hotelmanagement.assembler;

import com.akm.hotelmanagement.assembler.models.RoomModel;
import com.akm.hotelmanagement.controller.HotelAdminController;
import com.akm.hotelmanagement.dto.room.RoomResponseDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RoomModelAssembler extends RepresentationModelAssemblerSupport<RoomResponseDto, RoomModel>  {
    public RoomModelAssembler() {
        super(HotelAdminController.class, RoomModel.class);
    }

    @Override
    @NonNull public RoomModel toModel(@NonNull RoomResponseDto dto) {
        RoomModel roomModel = new RoomModel(dto);
        roomModel.add(
                linkTo(methodOn(HotelAdminController.class).getHotelRoomDetails(dto.getHotelId(), dto.getId(), null)).withSelfRel(),
                linkTo(methodOn(HotelAdminController.class).getHotelRoomReservations(dto.getHotelId(), dto.getId(), 0, 10, "id", "asc", null, null, null)).withRel("reservations"),
                linkTo(methodOn(HotelAdminController.class).getHotelDetails(dto.getHotelId(), null)).withRel("hotel")
        );
        return roomModel;
    }
}