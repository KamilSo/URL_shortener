# URL Shortener

Full-stack web application for generating and resolving shortened URLs.

## Tech

React | TypeScript | Java 21 | Spring Boot 3 | PostgreSQL | Vite | Maven

## Features

- Create shortened URLs
- Redirect short URLs to their original destination
- Persist URL mappings in PostgreSQL
- REST API between React frontend and Spring Boot backend
- Input validation / error handling

## Prerequisites

- **Java 21** — install a JDK 21 from [Adoptium](https://adoptium.net/) (or another vendor), then confirm with `java -version`
- **Maven** — optional; this repo includes the Maven Wrapper (`mvnw` / `mvnw.cmd`). To install Maven globally, see [maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)
- **Docker** — install [Docker Desktop](https://www.docker.com/products/docker-desktop/) (includes Docker Compose), then confirm with `docker compose version`
- **Node.js** and npm — install an LTS release from [nodejs.org](https://nodejs.org/), then confirm with `node -v` and `npm -v`

## Run locally

### 1. Start PostgreSQL

From the `server/` directory:

```bash
docker compose up -d
```

This starts Postgres on port `5432` with database `url_shortener` (user/password: `postgres` / `postgres`) and applies `schema.sql` on first startup.

### 2. Start the backend

From the `server/` directory:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

API runs at [http://localhost:5000](http://localhost:5000).

To compile the backend, run unit tests, and package the jar:

```bash
./mvnw clean verify
```

On Windows:

```bash
mvnw.cmd clean verify
```

### 3. Start the frontend

From the `client/` directory:

```bash
npm install
npm run dev
```

Frontend runs at the Vite default URL (usually [http://localhost:5173](http://localhost:5173)) and talks to the API on port `5000`.

## Working with `schema.sql`

[`server/schema.sql`](server/schema.sql) defines the `urls` table used by the API.

Docker Compose mounts it into `/docker-entrypoint-initdb.d/`, so Postgres runs it **only when the database volume is created for the first time**.

If you change the schema later:

1. Reset the volume and recreate the container:

   ```bash
   docker compose down -v
   docker compose up -d
   ```

2. Or apply the SQL yourself against the running database (for example with `psql`).
