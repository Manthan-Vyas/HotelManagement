package com.akm.hotelmanagement.util;

import java.util.Random;

public class TestStringUtils {
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_+=<>?";
    private static final String ALL = UPPER + LOWER + DIGITS + SPECIAL;
    private static final Random RANDOM = new Random();

    public static String generateRandomWord(int noOfLetters) {
        StringBuilder sb = new StringBuilder(noOfLetters);
        for (int i = 0; i < noOfLetters; i++) {
            char c = (char) (RANDOM.nextInt(26) + 'a');
            sb.append(c);
        }
        return sb.toString();
    }

    public static String generateRandomWord() {
        int noOfLetters = RANDOM.nextInt(2, 7);
        StringBuilder sb = new StringBuilder(noOfLetters);
        for (int i = 0; i < noOfLetters; i++) {
            char c = (char) (RANDOM.nextInt(26) + 'a');
            sb.append(c);
        }
        return sb.toString();
    }

    public static String generateRandomSentence(int noOfWords) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < noOfWords; i++) {
            sb.append(generateRandomWord(RANDOM.nextInt(10) + 1));
            sb.append(' ');
        }
        return sb.toString();
    }

    public static String generateRandomSentence() {
        int noOfWords = RANDOM.nextInt(5, 15);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < noOfWords; i++) {
            sb.append(generateRandomWord(RANDOM.nextInt(10) + 1));
            sb.append(' ');
        }
        return sb.toString();
    }

    public static String generateRandomEmail() {
        return generateRandomWord() + "@" + generateRandomWord() + ".com";
    }

    public static String generateRandomPhoneNumber() {
        return "0" + RANDOM.nextLong(1000000000L, 9999999999L);
    }

    public static String generateRandomDate() {
        return RANDOM.nextInt(1, 31) + "/" + RANDOM.nextInt(1, 12) + "/" + RANDOM.nextInt(1900, 2022);
    }

    public static String generateRandomTime() {
        return RANDOM.nextInt(0, 24) + ":" + RANDOM.nextInt(0, 60);
    }

    public static String generateRandomDateTime() {
        return generateRandomDate() + " " + generateRandomTime();
    }

    public static String generateRandomAddress() {
        return RANDOM.nextInt(1, 1000) + " " + generateRandomWord() + " Street, " + generateRandomWord() + ", " + generateRandomWord() + " " + RANDOM.nextInt(10000, 99999);
    }

    public static String generateRandomCity() {
        return generateRandomWord() + " City";
    }

    public static String generateRandomState() {
        return generateRandomWord() + " State";
    }

    public static String generateRandomDescription() {
        return generateRandomSentence();
    }

    public static String generateRandomName() {
        return generateRandomWord() + " " + generateRandomWord();
    }

    public static String generateRandomPassword() {
        StringBuilder password = new StringBuilder();

        // Ensure at least one character from each category
        password.append(UPPER.charAt(RANDOM.nextInt(UPPER.length())));
        password.append(LOWER.charAt(RANDOM.nextInt(LOWER.length())));
        password.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        password.append(SPECIAL.charAt(RANDOM.nextInt(SPECIAL.length())));
        for (int i = 4; i < RANDOM.nextInt(8, 16); i++) {
            password.append(ALL.charAt(RANDOM.nextInt(ALL.length())));
        }
        return shuffleString(password.toString());
    }

    public static String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = RANDOM.nextInt(characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }

    public static String generateRandomUsername() {
        return generateRandomWord() + RANDOM.nextInt(100, 999);
    }

    public static String generateRandomZipCode() {
        return String.valueOf(RANDOM.nextInt(10000, 99999));
    }

    public static String generateRandomImageUrl() {
        return "https://example.com/" + generateRandomWord() + ".png";
    }
}