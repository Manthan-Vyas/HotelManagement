package com.akm.hotelmanagement.assembler;

import com.akm.hotelmanagement.assembler.models.AmenityModel;
import com.akm.hotelmanagement.controller.AdminController;
import com.akm.hotelmanagement.controller.HotelAdminController;
import com.akm.hotelmanagement.controller.PublicController;
import com.akm.hotelmanagement.controller.UserController;
import com.akm.hotelmanagement.controller.base.BaseController;
import com.akm.hotelmanagement.dto.amenity.AmenityResponseDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static com.akm.hotelmanagement.util.Constants.*;
import static com.akm.hotelmanagement.util.Utils.getCurrentUserRole;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AmenityModelAssembler extends RepresentationModelAssemblerSupport<AmenityResponseDto, AmenityModel> {
    public AmenityModelAssembler() {
        super(BaseController.class, AmenityModel.class);
    }

    @Override
    @NonNull
    public AmenityModel toModel(@NonNull AmenityResponseDto dto) {
        AmenityModel amenityModel = new AmenityModel(dto);
        switch (getCurrentUserRole()) {
            case ADMIN:
                amenityModel.add(
                        linkTo(methodOn(AdminController.class).getAmenityDetails(dto.getId(), null))
                                .withSelfRel(),
                        linkTo(methodOn(AdminController.class).getHotelsByAmenityId(
                                dto.getId(),
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_AMENITY_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("amenity-hotels"),
                        linkTo(methodOn(AdminController.class).getAllAmenities(
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_AMENITY_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("all-amenities")
                );
                break;
            case HOTEL_ADMIN:
                amenityModel.add(
                        linkTo(methodOn(HotelAdminController.class).getAmenityDetails(dto.getId(), null))
                                .withSelfRel(),
                        linkTo(methodOn(HotelAdminController.class).getHotelsByAmenityId(
                                dto.getId(),
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_AMENITY_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("amenity-hotels"),
                        linkTo(methodOn(HotelAdminController.class).getAllAmenities(
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_AMENITY_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("amenities")
                );
                break;
            case USER:
                amenityModel.add(
                        linkTo(methodOn(UserController.class).getAmenityDetails(dto.getId(), null))
                                .withSelfRel(),
                        linkTo(methodOn(UserController.class).getHotelsByAmenityId(
                                dto.getId(),
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_AMENITY_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("amenity-hotels"),
                        linkTo(methodOn(UserController.class).getAllAmenities(
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_AMENITY_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("amenities")
                );
                break;
            default:
                amenityModel.add(
                        linkTo(methodOn(PublicController.class).getAmenityDetails(dto.getId(), null))
                                .withSelfRel(),
                        linkTo(methodOn(PublicController.class).getAllAmenities(
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_AMENITY_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("amenities"),
                        linkTo(methodOn(PublicController.class).getAllAmenities(
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_AMENITY_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("amenities")
                );
                break;
        }
        return amenityModel;
    }
}