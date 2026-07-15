import { Router } from "express";

import {
    createShortUrl,
    getAllUrls,
    redirectToOriginalUrl,
} from "../controllers/urlController";

export const urlRoutes = Router();

urlRoutes.post("/api/shorten", createShortUrl);

urlRoutes.get("/api/urls", getAllUrls);

urlRoutes.get("/:shortCode", redirectToOriginalUrl);