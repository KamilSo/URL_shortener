package com.urlshortener.utils;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class IsValidUrlTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "https://example.com",
            "HTTPS://example.com",
            "HtTpS://example.com",
            "http://example.com",
            "HTTP://example.com",
            "HtTp://example.com",
    })
    void acceptsHttpRegardlessOfCase(String url) {
        assertTrue(IsValidUrl.isValidUrl(url));
    }
    @Test
    void rejectsFtpUrl() {
        boolean result = IsValidUrl.isValidUrl("ftp://example.com");

        assertFalse(result);
    }

    @Test
    void rejectsNull() {
        boolean result = IsValidUrl.isValidUrl(null);

        assertFalse(result);
    }
    @ParameterizedTest
    @ValueSource(strings = {
            " ",
            "",
    })
    void rejectsEmpty(String url) {
        assertFalse(IsValidUrl.isValidUrl(url));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://example.co m",
            "https://exa mple.com",
            "https://examplecom.",
            "https:/example.com",
            "https//example.com",
            "https:example.com",
            "https://example,com",
            "https://",
            "https://example"
    })
    void rejectsInvalidSyntax(String url) {
        boolean result = IsValidUrl.isValidUrl(url);

        assertFalse(result);
    }
}