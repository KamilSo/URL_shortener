package com.urlshortener;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "*")
public class UrlShortenerApplication implements WebMvcConfigurer {

    private final JdbcTemplate jdbcTemplate;

    public UrlShortenerApplication(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(UrlShortenerApplication.class, args);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
    }

    @GetMapping("/")
    public String root() {
        return "URL Shortener API is running";
    }

    @GetMapping("/api/db-test")
    public Map<String, Object> dbTest() {
        var time = jdbcTemplate.queryForObject("SELECT NOW()", Object.class);

        return Map.of(
                "message", "Database connection works",
                "time", time
        );
    }
}
