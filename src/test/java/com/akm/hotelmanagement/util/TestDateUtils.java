package com.akm.hotelmanagement.util;

import java.time.LocalDate;
import java.util.Random;

public class TestDateUtils {
    public static LocalDate getToday() {
        return LocalDate.now();
    }

    public static LocalDate getRandomDate() {
        return LocalDate.now().minusDays(new Random().nextInt(30));
    }

    // random future date
    public static LocalDate getRandomFutureDate() {
        return LocalDate.now().plusDays(new Random().nextInt(30));
    }

    // random past date
    public static LocalDate getRandomPastDate() {
        return LocalDate.now().minusDays(new Random().nextInt(30));
    }

    public static LocalDate getTomorrow() {
        return LocalDate.now().plusDays(1);
    }

    public static LocalDate getYesterday() {
        return LocalDate.now().minusDays(1);
    }

    public static LocalDate getFutureDate(int days) {
        return LocalDate.now().plusDays(days);
    }

    public static LocalDate getPastDate(int days) {
        return LocalDate.now().minusDays(days);
    }

    public static LocalDate getFutureDate(LocalDate date, int days) {
        return date.plusDays(days);
    }
}
