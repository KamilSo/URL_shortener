package com.urlshortener.utils;

import java.util.concurrent.ThreadLocalRandom;

public final class GenerateShortCode {
    private static final String CHARACTERS =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private GenerateShortCode() {
    }

    public static String generateShortCode() {
        return generateShortCode(6);
    }

    public static String generateShortCode(int length) {
        StringBuilder shortCode = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(CHARACTERS.length());
            shortCode.append(CHARACTERS.charAt(randomIndex));
        }

        return shortCode.toString();
    }
}
