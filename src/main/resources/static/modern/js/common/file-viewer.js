export function isVideoFile(name) {
    const lower = String(name || "").toLowerCase();
    return lower.endsWith(".mp4") || lower.endsWith(".mkv") || lower.endsWith(".webm") || lower.endsWith(".m3u8");
}

export function isTextFile(name) {
    const lower = String(name || "").toLowerCase();
    return lower.endsWith(".txt") || lower.endsWith(".log") || lower.endsWith(".md") || lower.endsWith(".json")
        || lower.endsWith(".yml") || lower.endsWith(".yaml") || lower.endsWith(".xml") || lower.endsWith(".properties");
}

export function isImageFile(name) {
    const lower = String(name || "").toLowerCase();
    return lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".gif")
        || lower.endsWith(".webp") || lower.endsWith(".bmp") || lower.endsWith(".svg");
}
