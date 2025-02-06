package com.akm.hotelmanagement.util;

import com.akm.hotelmanagement.entity.util.ReservationStatus;
import com.akm.hotelmanagement.entity.util.RoomStatus;
import com.akm.hotelmanagement.entity.util.UserRole;

public class Constants {
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final int DEFAULT_PAGE_NUMBER_INT = 0;
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final int DEFAULT_PAGE_SIZE_INT = 10;
    public static final String DEFAULT_SORT_BY = "id";
    public static final String SORT_ASC = "asc";
    public static final String SORT_DESC = "desc";
    public static final String DEFAULT_SORT_DIR = SORT_ASC;
    public static final String DEFAULT_AMENITY_SORT_BY = "name";
    public static final String DEFAULT_HOTEL_SORT_BY = "name";
    public static final String DEFAULT_ROOM_SORT_BY = "number";
    public static final String DEFAULT_RESERVATION_SORT_BY = "checkIn";
    public static final String DEFAULT_USER_SORT_BY = "username";

    public static final UserRole DEFAULT_USER_ROLE = UserRole.USER;
    public static final String DEFAULT_USER_ROLE_STRING = DEFAULT_USER_ROLE.toString();
    public static final RoomStatus DEFAULT_ROOM_STATUS = RoomStatus.AVAILABLE;
    public static final String DEFAULT_ROOM_STATUS_STRING = DEFAULT_ROOM_STATUS.toString();
    public static final ReservationStatus DEFAULT_RESERVATION_STATUS = ReservationStatus.PENDING;
    public static final String DEFAULT_RESERVATION_STATUS_STRING = DEFAULT_RESERVATION_STATUS.toString();

    // validation constants
    public static final int AMENITY_NAME_MIN_LENGTH = 2;
    public static final int AMENITY_NAME_MAX_LENGTH = 20;
    public static final int AMENITY_DESCRIPTION_MIN_LENGTH = 5;
    public static final int AMENITY_DESCRIPTION_MAX_LENGTH = 50;

    public static final int HOTEL_NAME_MIN_LENGTH = 2;
    public static final int HOTEL_NAME_MAX_LENGTH = 50;
    public static final int HOTEL_ADDRESS_MIN_LENGTH = 5;
    public static final int HOTEL_ADDRESS_MAX_LENGTH = 100;
    public static final int HOTEL_CITY_MIN_LENGTH = 2;
    public static final int HOTEL_CITY_MAX_LENGTH = 50;
    public static final int HOTEL_STATE_MIN_LENGTH = 2;
    public static final int HOTEL_STATE_MAX_LENGTH = 50;
    public static final int HOTEL_ZIP_LENGTH = 6;
    public static final int HOTEL_DESCRIPTION_MIN_LENGTH = 5;
    public static final int HOTEL_DESCRIPTION_MAX_LENGTH = 200;
    public static final double HOTEL_RATING_MIN = 0;
    public static final double HOTEL_RATING_MAX = 5;
    public static final int HOTEL_RATING_MAX_INT = 5;

    public static final int ROOM_NUMBER_MIN_LENGTH = 1;
    public static final int ROOM_NUMBER_MAX_LENGTH = 10;
    public static final int ROOM_TYPE_MIN_LENGTH = 1;
    public static final int ROOM_TYPE_MAX_LENGTH = 50;
    public static final int ROOM_DESCRIPTION_MIN_LENGTH = 5;
    public static final int ROOM_DESCRIPTION_MAX_LENGTH = 200;


    public static final int RESERVATION_PRICE_MIN = 0;
    public static final int RESERVATION_PRICE_MAX = 1000000;
    public static final int RESERVATION_GUESTS_MIN = 1;
    public static final int RESERVATION_GUESTS_MAX = 10;
    public static final int USER_NAME_MIN_LENGTH = 2;
    public static final int USER_NAME_MAX_LENGTH = 50;
    public static final int USER_EMAIL_MIN_LENGTH = 5;
    public static final int USER_EMAIL_MAX_LENGTH = 50;
    public static final int USER_PHONE_LENGTH = 10;
    public static final int USER_USERNAME_MIN_LENGTH = 3;
    public static final int USER_USERNAME_MAX_LENGTH = 20;
    public static final int USER_PASSWORD_MIN_LENGTH = 8;
    public static final int USER_PASSWORD_MAX_LENGTH = 50;

    public static final String EMAIL_PATTERN = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
//    public static final String EMAIL_PATTERN = ".*@.*\\..*";
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,50}$";
    public static final String PHONE_PATTERN = "^\\d{10}$";
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]*$";
    public static final String NAME_PATTERN = "^[a-zA-Z ]*$";
    public static final String ZIP_PATTERN = "^\\d{6}$";
}