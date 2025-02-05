package com.akm.hotelmanagement.assembler;

import com.akm.hotelmanagement.assembler.models.RoomModel;
import com.akm.hotelmanagement.controller.AdminController;
import com.akm.hotelmanagement.controller.HotelAdminController;
import com.akm.hotelmanagement.controller.PublicController;
import com.akm.hotelmanagement.controller.UserController;
import com.akm.hotelmanagement.dto.room.RoomResponseDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static com.akm.hotelmanagement.util.Constants.*;
import static com.akm.hotelmanagement.util.Utils.getCurrentUserRole;
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
        switch (getCurrentUserRole()) {
            case ADMIN:
                roomModel.add(
                        linkTo(methodOn(AdminController.class).getRoomDetails(dto.getId(), null)).withSelfRel(),
                        linkTo(methodOn(AdminController.class).getAmenitiesByRoomId(
                                dto.getId(),
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_AMENITY_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("room-amenities"),
                        linkTo(methodOn(AdminController.class).getAllRooms(
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_ROOM_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("all-rooms")
                );
                break;
            case HOTEL_ADMIN:
                roomModel.add(
                        linkTo(methodOn(HotelAdminController.class).getRoomDetails(dto.getId(), null)).withSelfRel(),
                        linkTo(methodOn(HotelAdminController.class).getHotelRoomReservations(
                                dto.getHotelId(), dto.getId(),
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_ROOM_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("room-reservations"),
                        linkTo(methodOn(HotelAdminController.class).getAmenitiesByRoomId(
                                dto.getId(),
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_AMENITY_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("room-amenities"),
                        linkTo(methodOn(HotelAdminController.class).getAllRooms(
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_ROOM_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("rooms")
                );
                break;
            case USER:
                roomModel.add(
                        linkTo(methodOn(UserController.class).getRoomDetails(dto.getId(), null)).withSelfRel(),
                        linkTo(methodOn(UserController.class).getAmenitiesByRoomId(
                                dto.getId(),
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_AMENITY_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("room-amenities"),
                        linkTo(methodOn(UserController.class).getAllRooms(
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_ROOM_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("rooms")
                );
                break;
            default:
                roomModel.add(
                        linkTo(methodOn(PublicController.class).getRoomDetails(dto.getId(), null)).withSelfRel(),
                        linkTo(methodOn(PublicController.class).getAmenitiesByRoomId(
                                dto.getId(),
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_AMENITY_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("room-amenities"),
                        linkTo(methodOn(PublicController.class).getAllRooms(
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_ROOM_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("rooms")
                );
                break;
        }
        return roomModel;
    }
}