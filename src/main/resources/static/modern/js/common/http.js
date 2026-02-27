import {getToken} from "./auth.js";

const { ElMessage, ElMessageBox } = window.ElementPlus;

function withTokenHeaders(headers) {
    const finalHeaders = headers || {};
    finalHeaders.token = getToken();
    return finalHeaders;
}

async function request(method, url, data, extra) {
    const cfg = {
        method,
        url,
        headers: withTokenHeaders(extra && extra.headers),
        ...extra
    };
    if (data !== undefined) {
        cfg.data = data;
    }
    const res = await window.axios(cfg);
    const body = res.data || {};
    if (body.code !== 200) {
        await ElMessageBox.alert(body.message || "请求失败", "错误", { type: "error" });
        throw new Error(body.message || "request failed");
    }
    return body.data;
}

export function get(url, extra) {
    return request("get", url, undefined, extra);
}

export function post(url, data, extra) {
    return request("post", url, data, {
        headers: { "Content-Type": "application/json", ...(extra && extra.headers ? extra.headers : {}) },
        ...extra
    });
}

export function ok(message) {
    ElMessage.success(message);
}

export function warn(message) {
    ElMessage.warning(message);
}

export async function confirm(message, title) {
    try {
        await ElMessageBox.confirm(message, title || "确认", { type: "warning" });
        return true;
    } catch (e) {
        return false;
    }
}

export function normalizePageResult(data) {
    return {
        list: data && data.list ? data.list : [],
        total: data && data.total ? data.total : 0,
        pageNum: data && data.pageNum ? data.pageNum : 1,
        pageSize: data && data.pageSize ? data.pageSize : 10,
        pages: data && data.pages ? data.pages : 0,
        hasNextPage: !!(data && data.hasNextPage),
        hasPreviousPage: !!(data && data.hasPreviousPage)
    };
}

export function encodePath(path) {
    if (!path) {
        return "";
    }
    return path
        .split("/")
        .filter(Boolean)
        .map((item) => encodeURIComponent(item))
        .join("/");
}
