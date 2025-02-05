package com.akm.hotelmanagement.assembler;

import com.akm.hotelmanagement.assembler.models.ReservationModel;
import com.akm.hotelmanagement.controller.AdminController;
import com.akm.hotelmanagement.controller.HotelAdminController;
import com.akm.hotelmanagement.controller.UserController;
import com.akm.hotelmanagement.dto.reservation.ReservationResponseDto;
import com.akm.hotelmanagement.mapper.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static com.akm.hotelmanagement.util.Constants.*;
import static com.akm.hotelmanagement.util.Utils.getCurrentUserRole;
import static com.akm.hotelmanagement.util.Utils.getCurrentUsername;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReservationModelAssembler extends RepresentationModelAssemblerSupport<ReservationResponseDto, ReservationModel> {
    private final ReservationMapper reservationMapper;

    @Autowired
    public ReservationModelAssembler(ReservationMapper reservationMapper
    ) {
        super(AdminController.class, ReservationModel.class);
        this.reservationMapper = reservationMapper;
    }

    @Override
    @NonNull
    public ReservationModel toModel(@NonNull ReservationResponseDto dto) {
        ReservationModel reservationModel = new ReservationModel(dto);
        switch (getCurrentUserRole()) {
            case ADMIN:
                reservationModel.add(
                        linkTo(methodOn(AdminController.class).getReservationDetails(dto.getId(), null)).withSelfRel(),
                        linkTo(methodOn(AdminController.class).getHotelByReservationId(dto.getId(), null)).withRel("hotel"),
                        linkTo(methodOn(AdminController.class).getRoomByReservationId(dto.getId(), null)).withRel("room"),
                        linkTo(methodOn(AdminController.class).getUserByReservationId(dto.getId(), null)).withRel("user"),
                        linkTo(methodOn(AdminController.class).getAllReservations(
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_RESERVATION_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("all-reservations"));
                break;
            case HOTEL_ADMIN:
                reservationModel.add(
                        linkTo(methodOn(HotelAdminController.class).getReservationDetails(dto.getId(), null)).withSelfRel(),
                        linkTo(methodOn(HotelAdminController.class).getRoomByReservationId(
                                reservationMapper.toEntity(dto).getRoom().getHotel().getId(),
                                dto.getRoomId(),
                                dto.getId(),
                                null
                        )).withRel("room"),
                        linkTo(methodOn(HotelAdminController.class).getAllReservations(
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_RESERVATION_SORT_BY, DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("reservations"));
               break;
            case USER:
                reservationModel.add(
                        linkTo(methodOn(UserController.class).getUserReservation(getCurrentUsername(), dto.getId(), null)).withSelfRel(),
                        linkTo(methodOn(UserController.class).getHotelByReservationId(getCurrentUsername(), dto.getId(), null)).withRel("hotel"),
                        linkTo(methodOn(UserController.class).getRoomByReservationId(getCurrentUsername(), dto.getId(), null)).withRel("room"),
                        linkTo(methodOn(UserController.class).getUserReservations(
                                getCurrentUsername(),
                                DEFAULT_PAGE_NUMBER_INT,
                                DEFAULT_PAGE_SIZE_INT,
                                DEFAULT_RESERVATION_SORT_BY,
                                DEFAULT_SORT_DIR,
                                null, null, null
                        )).withRel("reservations"));
                break;
            default:
                break;
        }
        return reservationModel;
    }
}
