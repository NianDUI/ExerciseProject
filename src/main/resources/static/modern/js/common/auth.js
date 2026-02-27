const PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJg2Zsf4SrrGAJMSrfsjNJqyk9KxXNqcZUGttaIp/rsBJYyXidXR9r5NbYZ5ahqmNjSlWWxYmMfkPpe5nUFQyxsCAwEAAQ==";

function encryptPlainToken(plain) {
    const enc = new window.JSEncrypt();
    enc.setPublicKey("-----BEGIN PUBLIC KEY-----" + PUBLIC_KEY + "-----END PUBLIC KEY-----");
    return enc.encrypt(plain);
}

export function setTokenByPrompt() {
    let plain = "";
    while (!plain || !plain.trim()) {
        plain = window.prompt("请输入 token：") || "";
    }
    const encrypted = encryptPlainToken(plain.trim());
    if (!encrypted) {
        throw new Error("Token 加密失败");
    }
    window.sessionStorage.setItem("token", encrypted);
    window.Cookies.set("token", encrypted);
    return encrypted;
}

export function getToken() {
    let token = window.Cookies.get("token");
    if (!token) {
        token = window.sessionStorage.getItem("token");
    }
    if (!token) {
        token = setTokenByPrompt();
    }
    return token;
}

export function clearToken() {
    window.sessionStorage.removeItem("token");
    window.Cookies.remove("token");
}
