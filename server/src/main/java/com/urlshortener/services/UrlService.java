package com.urlshortener.services;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.urlshortener.models.Url;
import com.urlshortener.utils.GenerateShortCode;

@Service
public class UrlService {
    private static final int MAX_SHORT_CODE_ATTEMPTS = 5;

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<UrlRow> urlRowMapper = (rs, rowNum) -> new UrlRow(
            rs.getLong("id"),
            rs.getString("original_url"),
            rs.getString("short_code"),
            rs.getInt("click_count"),
            toOffsetDateTime(rs.getTimestamp("created_at"))
    );

    public UrlService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Url createUrl(String originalUrl) {
        for (int attempt = 1; attempt <= MAX_SHORT_CODE_ATTEMPTS; attempt++) {
            String shortCode = GenerateShortCode.generateShortCode();

            try {
                UrlRow row = jdbcTemplate.queryForObject(
                        """
                                INSERT INTO urls (original_url, short_code)
                                VALUES (?, ?)
                                RETURNING id, original_url, short_code, click_count, created_at
                                """,
                        urlRowMapper,
                        originalUrl,
                        shortCode
                );

                return mapRowToUrl(row);
            } catch (DuplicateKeyException error) {
                if (attempt < MAX_SHORT_CODE_ATTEMPTS) {
                    continue;
                }

                throw error;
            }
        }

        throw new RuntimeException("Could not generate a unique short code.");
    }

    public List<Url> getUrls() {
        List<UrlRow> rows = jdbcTemplate.query(
                """
                        SELECT id, original_url, short_code, click_count, created_at
                        FROM urls
                        ORDER BY created_at DESC
                        """,
                urlRowMapper
        );

        return rows.stream().map(this::mapRowToUrl).toList();
    }

    public Url findUrlByShortCode(String shortCode) {
        List<UrlRow> rows = jdbcTemplate.query(
                """
                        SELECT id, original_url, short_code, click_count, created_at
                        FROM urls
                        WHERE short_code = ?
                        """,
                urlRowMapper,
                shortCode
        );

        if (rows.isEmpty()) {
            return null;
        }

        UrlRow urlRecord = rows.get(0);

        jdbcTemplate.update(
                """
                        UPDATE urls
                        SET click_count = click_count + 1
                        WHERE short_code = ?
                        """,
                shortCode
        );

        return mapRowToUrl(new UrlRow(
                urlRecord.id(),
                urlRecord.originalUrl(),
                urlRecord.shortCode(),
                urlRecord.clickCount() + 1,
                urlRecord.createdAt()
        ));
    }

    private Url mapRowToUrl(UrlRow row) {
        return new Url(
                row.id(),
                row.originalUrl(),
                row.shortCode(),
                "http://localhost:5000/" + row.shortCode(),
                row.clickCount(),
                row.createdAt()
        );
    }

    private static OffsetDateTime toOffsetDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }

        return timestamp.toInstant().atOffset(ZoneOffset.UTC);
    }

    private record UrlRow(
            long id,
            String originalUrl,
            String shortCode,
            int clickCount,
            OffsetDateTime createdAt
    ) {
    }
}
