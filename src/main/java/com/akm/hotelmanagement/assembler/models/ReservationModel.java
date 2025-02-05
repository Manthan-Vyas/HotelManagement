package com.akm.hotelmanagement.assembler.models;

import com.akm.hotelmanagement.dto.reservation.ReservationResponseDto;
import com.akm.hotelmanagement.entity.util.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationModel extends RepresentationModel<ReservationModel> {
    private Long id;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int numberOfGuests;
    private double totalPrice;
    private LocalDate reservationDate;
    private ReservationStatus status;

    public ReservationModel(ReservationResponseDto dto) {
        this(
                dto.getId(),
                dto.getCheckIn(),
                dto.getCheckOut(),
                dto.getNumberOfGuests(),
                dto.getTotalPrice(),
                dto.getReservationDate(),
                dto.getStatus()
        );
    }
}