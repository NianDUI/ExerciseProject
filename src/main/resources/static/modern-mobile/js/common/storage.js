const READER_PROGRESS_KEY = "fictionweb.mobile.reader.progress";
const READER_PREF_KEY = "fictionweb.mobile.reader.pref";

function readJson(key, fallback) {
    try {
        const raw = window.localStorage.getItem(key);
        if (!raw) {
            return fallback;
        }
        return JSON.parse(raw);
    } catch (e) {
        return fallback;
    }
}

function writeJson(key, value) {
    window.localStorage.setItem(key, JSON.stringify(value));
}

export function getProgressMap() {
    return readJson(READER_PROGRESS_KEY, {});
}

export function getBookProgress(bookId) {
    const map = getProgressMap();
    return bookId ? map[String(bookId)] || null : null;
}

export function saveBookProgress(payload) {
    if (!payload || !payload.bookId) {
        return;
    }
    const map = getProgressMap();
    map[String(payload.bookId)] = {
        ...payload,
        updatedAt: new Date().toISOString()
    };
    writeJson(READER_PROGRESS_KEY, map);
}

export function getRecentProgressList(limit) {
    return Object.values(getProgressMap())
        .sort((a, b) => String(b.updatedAt || "").localeCompare(String(a.updatedAt || "")))
        .slice(0, limit || 10);
}

export function getReaderPreference() {
    return {
        fontSize: 19,
        lineHeight: 1.95,
        theme: "paper",
        ...readJson(READER_PREF_KEY, {})
    };
}

export function saveReaderPreference(pref) {
    writeJson(READER_PREF_KEY, {
        ...getReaderPreference(),
        ...pref
    });
}

export function clearMobileCache() {
    window.localStorage.removeItem(READER_PROGRESS_KEY);
}
