package com.akm.hotelmanagement.assembler;

import com.akm.hotelmanagement.assembler.models.AmenityModel;
import com.akm.hotelmanagement.controller.AdminController;
import com.akm.hotelmanagement.dto.amenity.AmenityResponseDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AmenityModelAssembler extends RepresentationModelAssemblerSupport<AmenityResponseDto, AmenityModel> {
    public AmenityModelAssembler() {
        super(AdminController.class, AmenityModel.class);
    }

    @Override
    @NonNull
    public AmenityModel toModel(@NonNull AmenityResponseDto dto) {
        AmenityModel amenityModel = new AmenityModel(dto);
        amenityModel.add(
                linkTo(methodOn(AdminController.class).getAmenityDetails(dto.getId(), null)).withSelfRel(),
                linkTo(methodOn(AdminController.class).getAllAmenities(
                        0, 10, "name", "asc", null, null, null
                )).withRel("allAmenities"),
                linkTo(methodOn(AdminController.class).getAmenityHotels(dto.getId(), 0, 10, "name", "asc", null, null, null)).withRel("hotels")
        );
        return amenityModel;
    }
}