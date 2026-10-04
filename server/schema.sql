CREATE TABLE IF NOT EXISTS urls (
    id            SERIAL PRIMARY KEY,
    original_url  TEXT NOT NULL,
    short_code    VARCHAR(6) NOT NULL UNIQUE,
    click_count   INTEGER NOT NULL DEFAULT 0,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
