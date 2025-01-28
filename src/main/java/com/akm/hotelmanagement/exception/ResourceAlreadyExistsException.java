package com.akm.hotelmanagement.exception;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends HotelManagementApiException {
    public ResourceAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
