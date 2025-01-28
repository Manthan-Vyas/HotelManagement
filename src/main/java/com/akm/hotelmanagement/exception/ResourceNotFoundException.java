package com.akm.hotelmanagement.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends HotelManagementApiException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
