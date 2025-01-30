package com.akm.hotelmanagement.assembler;

import com.akm.hotelmanagement.assembler.models.HotelModel;
import com.akm.hotelmanagement.controller.HotelAdminController;
import com.akm.hotelmanagement.dto.hotel.HotelResponseDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HotelModelAssembler extends RepresentationModelAssemblerSupport<HotelResponseDto, HotelModel> {
    public HotelModelAssembler() {
        super(HotelAdminController.class, HotelModel.class);
    }

    @Override
    @NonNull
    public HotelModel toModel(@NonNull HotelResponseDto dto) {
        HotelModel hotelModel = new HotelModel(dto);
        hotelModel.add(
                linkTo(methodOn(HotelAdminController.class).getHotelDetails(dto.getId(), null)).withSelfRel(),
                linkTo(methodOn(HotelAdminController.class).getHotelRooms(dto.getId(), 0, 10, "number", "acs", null, null, null)).withRel("rooms")
        );
        return hotelModel;
    }
}