/*
package com.backend.NawaBari.jwt;

import java.security.SecureRandom;
import java.util.Random;

public class PasswordUtil {
    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int PASSWORD_LENGTH = 10;

    public static String generateRandomPassword() {
        StringBuilder passwordBuilder = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            passwordBuilder.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return passwordBuilder.toString();
    }
}
*/
