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
        assertTrue(IsValidUrl.isValidHttpUrl(url));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ftp://example.com",
            "file:///tmp/example.txt",
            "irc://irc.example.com/channel",
            "mailto:person@example.com"
    })
    void rejectsUnsupportedSchemes(String url) {
        assertFalse(IsValidUrl.isValidHttpUrl(url));
    }

    @Test
    void rejectsNull() {
        assertFalse(IsValidUrl.isValidHttpUrl(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            " ",
            "",
    })
    void rejectsEmpty(String url) {
        assertFalse(IsValidUrl.isValidHttpUrl(url));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://example.co m",
            "https://exa mple.com",
            "https:/example.com",
            "https//example.com",
            "https:example.com",
            "https://example,com",
            "https://",
    })
    void rejectsInvalidSyntax(String url) {
        assertFalse(IsValidUrl.isValidHttpUrl(url));
    }
}