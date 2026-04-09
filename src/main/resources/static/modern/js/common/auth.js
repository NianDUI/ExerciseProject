const PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJg2Zsf4SrrGAJMSrfsjNJqyk9KxXNqcZUGttaIp/rsBJYyXidXR9r5NbYZ5ahqmNjSlWWxYmMfkPpe5nUFQyxsCAwEAAQ==";

const { ElMessageBox, ElMessage } = window.ElementPlus;

function encryptPlainToken(plain) {
    const enc = new window.JSEncrypt();
    enc.setPublicKey("-----BEGIN PUBLIC KEY-----" + PUBLIC_KEY + "-----END PUBLIC KEY-----");
    return enc.encrypt(plain);
}

function saveToken(encrypted) {
    window.sessionStorage.setItem("token", encrypted);
    window.Cookies.set("token", encrypted);
}

export async function setTokenByPrompt() {
    try {
        const { value } = await ElMessageBox.prompt("请输入 Token", "设置 Token", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            inputPlaceholder: "请输入 token",
            inputValidator: (val) => {
                if (!val || !val.trim()) return "Token 不能为空";
                return true;
            }
        });
        const encrypted = encryptPlainToken(value.trim());
        if (!encrypted) {
            ElMessage.error("Token 加密失败");
            return null;
        }
        saveToken(encrypted);
        ElMessage.success("Token 更新成功");
        return encrypted;
    } catch {
        return null;
    }
}

export function getToken() {
    let token = window.Cookies.get("token");
    if (!token) {
        token = window.sessionStorage.getItem("token");
    }
    if (!token) {
        setTokenByPrompt();
        return "";
    }
    return token;
}

export function clearToken() {
    window.sessionStorage.removeItem("token");
    window.Cookies.remove("token");
}
