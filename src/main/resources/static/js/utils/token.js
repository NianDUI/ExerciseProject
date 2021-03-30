/* token相关方法 */

// 获取请求头
function headers() {
    return {"token": getToken()};
}

// 获取token
function getToken() {
    let token = sessionStorage.getItem("token");
    if (token == null || token.length === 0) {
        do {
            token = prompt("请输入：");
        } while (token.trim().length === 0)
        // 加密
        token = encrypt.encrypt(token);
        sessionStorage.setItem("token", token);
    }
    return token;
}

// 处理Url token
function handleUrlToken(token) {
    return token.replace("+", "%2B");
}