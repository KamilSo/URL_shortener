import express from "express";
import cors from "cors";
import { urlRoutes } from "./routes/urlRoutes";
import { pool } from "./db";

const app = express();
const PORT = 5000;

app.use(cors());
app.use(express.json());

app.get("/api/db-test", async (req, res) => {
    const result = await pool.query("SELECT NOW()");

    res.json({
        message: "Database connection works",
        time: result.rows[0].now,
    });
});

app.get("/", (req, res) => {
    res.send("URL Shortener API is running");
});

app.use("/", urlRoutes);

app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});