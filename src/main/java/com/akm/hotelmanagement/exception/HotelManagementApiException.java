package com.akm.hotelmanagement.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HotelManagementApiException extends RuntimeException {
    protected HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public HotelManagementApiException(String message) {
        super(message);
    }

    public HotelManagementApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
