import { Request, Response } from "express";

import {
    createUrl,
    getUrls,
    findUrlByShortCode,
} from "../services/urlService";

import { isValidUrl } from "../utils/isValidUrl";

export async function createShortUrl(req: Request, res: Response) {
    try {
        const originalUrl = req.body.originalUrl;

        if (typeof originalUrl !== "string" || !isValidUrl(originalUrl)) {
            res.status(400).json({
                error: "Please provide a valid http or https URL.",
            });
            return;
        }

        const result = await createUrl(originalUrl);

        res.status(201).json(result);
    } catch (error) {
        console.error(error);

        res.status(500).json({
            error: "Something went wrong while creating the short URL.",
        });
    }
}

export async function getAllUrls(req: Request, res: Response) {
    try {
        const urls = await getUrls();

        res.json(urls);
    } catch (error) {
        console.error(error);

        res.status(500).json({
            error: "Something went wrong while getting URLs.",
        });
    }
}

export async function redirectToOriginalUrl(req: Request, res: Response) {
    try {
        const shortCode = req.params.shortCode;

        const urlRecord = await findUrlByShortCode(shortCode);

        if (!urlRecord) {
            res.status(404).send("Short URL not found");
            return;
        }

        res.redirect(urlRecord.originalUrl);
    } catch (error) {
        console.error(error);

        res.status(500).send("Something went wrong.");
    }
}