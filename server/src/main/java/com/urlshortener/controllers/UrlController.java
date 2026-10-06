package com.urlshortener.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.urlshortener.models.Url;
import com.urlshortener.services.UrlService;
import com.urlshortener.utils.IsValidUrl;

@RestController
@CrossOrigin(origins = "*")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<?> createShortUrl(@RequestBody Map<String, Object> body) {
        try {
            Object originalUrlValue = body.get("originalUrl");

            if (!(originalUrlValue instanceof String originalUrl) || !IsValidUrl.isValidHttpUrl(originalUrl)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Please provide a valid http or https URL."));
            }

            Url result = urlService.createUrl(originalUrl);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception error) {
            System.err.println(error);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong while creating the short URL."));
        }
    }

    @GetMapping("/api/urls")
    public ResponseEntity<?> getAllUrls() {
        try {
            List<Url> urls = urlService.getUrls();

            return ResponseEntity.ok(urls);
        } catch (Exception error) {
            System.err.println(error);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong while getting URLs."));
        }
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortCode) {
        try {
            Url urlRecord = urlService.findUrlByShortCode(shortCode);

            if (urlRecord == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Short URL not found");
            }

            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .header("Location", urlRecord.getOriginalUrl())
                    .build();
        } catch (Exception error) {
            System.err.println(error);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong.");
        }
    }
}
