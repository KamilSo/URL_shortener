package com.urlshortener.utils;

import java.net.URI;

public final class IsValidUrl {
    private IsValidUrl() {
    }

    public static boolean isValidHttpUrl(String value) {
        try {
            URI uri = URI.create(value);
            String scheme = uri.getScheme();
            String host = uri.getHost();

            return ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) && host != null;
        } catch (Exception e) {
            return false;
        }
    }
}
