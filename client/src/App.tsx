import { useState } from "react";

type ShortenResponse = {
    id: number;
    originalUrl: string;
    shortCode: string;
    shortUrl: string;
    clickCount: number;
    createdAt: string;
};
function App() {
    const [originalUrl, setOriginalUrl] = useState("");
    const [shortenedUrl, setShortenedUrl] = useState<ShortenResponse | null>(null);
    const [error, setError] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [copied, setCopied] = useState(false);

    async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();

        setError("");
        setShortenedUrl(null);
        setCopied(false);
        setIsLoading(true);

        try {
            const response = await fetch("http://localhost:5000/api/shorten", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    originalUrl: originalUrl,
                }),
            });

            const data = await response.json();

            if (!response.ok) {
                setError(data.error || "Something went wrong.");
                return;
            }

            setShortenedUrl(data);
            setOriginalUrl("");
        } catch {
            setError("Could not connect to the server.");
        } finally {
            setIsLoading(false);
        }
    }
    async function handleCopy() {
        if (!shortenedUrl) {
            return;
        }

        await navigator.clipboard.writeText(shortenedUrl.shortUrl);

        setCopied(true);

        setTimeout(() => {
            setCopied(false);
        }, 2000);
    }
    return (
        <main className="page">
            <section className="card">
                <div className="header">
                    <p className="eyebrow">Simple · Fast · Shareable</p>
                    <h1>URL Shortener</h1>
                    <p className="subtitle">
                        Paste a long URL below and generate a clean short link.
                    </p>
                </div>

                <form className="url-form" onSubmit={handleSubmit}>
                    <input
                        type="text"
                        placeholder="https://example.com/very/long/url"
                        value={originalUrl}
                        onChange={(event) => setOriginalUrl(event.target.value)}
                    />

                    <button type="submit" disabled={isLoading}>
                        {isLoading ? "Shortening..." : "Shorten URL"}
                    </button>
                </form>

                {error && <p className="error-message">{error}</p>}

                {shortenedUrl && (
                    <div className="result-box">
                        <div className="result-header">
                            <div>
                                <p className="result-label">Your short URL is ready</p>
                                <a
                                    className="short-link"
                                    href={shortenedUrl.shortUrl}
                                    target="_blank"
                                    rel="noreferrer"
                                >
                                    {shortenedUrl.shortUrl}
                                </a>
                            </div>

                            <button
                                className="copy-button"
                                type="button"
                                onClick={handleCopy}
                            >
                                {copied ? "Copied!" : "Copy"}
                            </button>
                        </div>
                    </div>
                )}

                <p className="hint">
                    Your shortened links are saved and tracked using PostgreSQL.
                </p>
            </section>
        </main>
    );
}

export default App;