import { generateShortCode } from "../utils/generateShortCode";
import { pool } from "../db";

const MAX_SHORT_CODE_ATTEMPTS = 5;

type UrlRow = {
    id: number;
    original_url: string;
    short_code: string;
    click_count: number;
    created_at: Date;
};

function mapRowToUrl(row: UrlRow) {
    return {
        id: row.id,
        originalUrl: row.original_url,
        shortCode: row.short_code,
        shortUrl: `http://localhost:5000/${row.short_code}`,
        clickCount: row.click_count,
        createdAt: row.created_at,
    };
}

function isUniqueViolationError(error: unknown): boolean {
    return (
        typeof error === "object" &&
        error !== null &&
        "code" in error &&
        error.code === "23505"
    );
}
export async function createUrl(originalUrl: string) {
    for(let attempt = 1; attempt <= MAX_SHORT_CODE_ATTEMPTS; attempt++){
        const shortCode = generateShortCode();
        try{
            const result = await pool.query<UrlRow>(
                `
                    INSERT INTO urls (original_url, short_code)
                    VALUES ($1,$2)
                        RETURNING id, original_url, short_code, click_count, created_at;
                `,
                [originalUrl, shortCode]
            )
            return mapRowToUrl(result.rows[0]);
        } catch (error) {
            if (isUniqueViolationError(error) && attempt < MAX_SHORT_CODE_ATTEMPTS) {
                continue;
            }

            throw error;
        }
    }

    throw new Error("Could not generate a unique short code.");
}

export async function getUrls() {
    const result = await pool.query<UrlRow>(
        `
    SELECT id, original_url, short_code, click_count, created_at
    FROM urls
    ORDER BY created_at DESC;
    `
    );

    return result.rows.map(mapRowToUrl);
}

export async function findUrlByShortCode(shortCode: string) {
    const result = await pool.query<UrlRow>(
        `
    SELECT id, original_url, short_code, click_count, created_at
    FROM urls
    WHERE short_code = $1;
    `,
        [shortCode]
    );

    const urlRecord = result.rows[0];

    if (!urlRecord) {
        return null;
    }

    await pool.query(
        `
    UPDATE urls
    SET click_count = click_count + 1
    WHERE short_code = $1;
    `,
        [shortCode]
    );

    return mapRowToUrl({
        ...urlRecord,
        click_count: urlRecord.click_count + 1,
    });
}