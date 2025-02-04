package com.akm.hotelmanagement.assembler;

import com.akm.hotelmanagement.assembler.models.HotelModel;
import com.akm.hotelmanagement.controller.AdminController;
import com.akm.hotelmanagement.controller.HotelAdminController;
import com.akm.hotelmanagement.controller.PublicController;
import com.akm.hotelmanagement.controller.UserController;
import com.akm.hotelmanagement.dto.hotel.HotelResponseDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static com.akm.hotelmanagement.util.Constants.*;
import static com.akm.hotelmanagement.util.Utils.getCurrentUserRole;
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
        switch (getCurrentUserRole()) {
            case ADMIN:
                hotelModel.add(
                        linkTo(methodOn(AdminController.class).getHotelDetails(dto.getId(), null)).withSelfRel(),
                        linkTo(methodOn(AdminController.class).getAmenitiesByHotelId(
                                dto.getId(),
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_HOTEL_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("hotel-amenities"),
                        linkTo(methodOn(AdminController.class).getRoomsByHotelId(
                                dto.getId(),
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_HOTEL_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("hotel-rooms"),
                        linkTo(methodOn(HotelAdminController.class).getAllHotels(
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_HOTEL_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("all-hotels")
                );
                break;
            case HOTEL_ADMIN:
                hotelModel.add(
                        linkTo(methodOn(HotelAdminController.class).getHotelDetails(dto.getId(), null)).withSelfRel(),
                        linkTo(methodOn(HotelAdminController.class).getAmenitiesByHotelId(
                                dto.getId(),
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_HOTEL_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("hotel-amenities"),
                        linkTo(methodOn(HotelAdminController.class).getRoomsByHotelId(
                                dto.getId(),
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_HOTEL_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("hotel-rooms"),
                        linkTo(methodOn(HotelAdminController.class).getHotelReservations(
                                dto.getId(),
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_HOTEL_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("hotel-reservations"),
                        linkTo(methodOn(HotelAdminController.class).getAllHotels(
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_HOTEL_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("all-hotels")
                );
                break;
            case USER:
                hotelModel.add(
                        linkTo(methodOn(UserController.class).getHotelDetails(dto.getId(), null)).withSelfRel(),
                        linkTo(methodOn(UserController.class).getAmenitiesByHotelId(
                                dto.getId(),
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_HOTEL_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("hotel-amenities"),
                        linkTo(methodOn(UserController.class).getRoomsByHotelId(
                                dto.getId(),
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_HOTEL_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("hotel-rooms"),
                        linkTo(methodOn(UserController.class).getAllHotels(
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_HOTEL_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("hotels")
                );
                break;
            default:
                hotelModel.add(
                        linkTo(methodOn(PublicController.class).getHotelDetails(dto.getId(), null)).withSelfRel(),
                        linkTo(methodOn(PublicController.class).getAmenitiesByHotelId(
                                dto.getId(),
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_HOTEL_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("hotel-amenities"),
                        linkTo(methodOn(PublicController.class).getRoomsByHotelId(
                                dto.getId(),
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_HOTEL_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("hotel-rooms"),
                        linkTo(methodOn(PublicController.class).getAllHotels(
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_HOTEL_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("hotels")
                );
                break;
        }
        return hotelModel;
    }
}