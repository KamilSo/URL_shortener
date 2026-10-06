package com.urlshortener.utils;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenerateShortCodeTest {

    @Test
    void generatesSixCharactersByDefault() {
        String code = GenerateShortCode.generateShortCode();

        assertEquals(6, code.length());
    }
    @ParameterizedTest
    @ValueSource(ints = {1, 6, 10, 100})
    void generatesRequestedLength(int length) {
        String code = GenerateShortCode.generateShortCode(length);

        assertEquals(length, code.length());
    }
    @Test
    void containsOnlyLettersAndDigits() {
        String code = GenerateShortCode.generateShortCode(100);

        assertTrue(code.matches("[a-zA-Z0-9]+"));
    }
    @Test
    void returnsEmptyStringForZeroLength() {
        String code = GenerateShortCode.generateShortCode(0);

        assertEquals("", code);
    }
}