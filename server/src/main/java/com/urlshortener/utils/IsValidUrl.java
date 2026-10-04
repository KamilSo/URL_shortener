package com.urlshortener.utils;

import java.net.URI;

public final class IsValidUrl {
    private IsValidUrl() {
    }

    public static boolean isValidUrl(String value) {
        try {
            URI uri = URI.create(value);
            String scheme = uri.getScheme();

            return "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
        } catch (Exception e) {
            return false;
        }
    }
}
