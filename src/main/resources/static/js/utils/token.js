/* token相关方法 */

// 获取请求头
function headers() {
    return {"token": getToken()};
}

// 处理Url token
function handleUrlToken(token) {
    return token.replace("+", "%2B");
}

// 获取token
function getToken() {
    let token = Cookies.get("token");
    console.log("Cookies" + token)
    if (token == null || token.length === 0) {
        token = sessionStorage.getItem("token");
        console.log("sessionStorage" + token)
        if (token == null || token.length === 0) {
            // 设置token
            token = setToken();
        }
    }
    return token;
}

// 设置token
function setToken() {
    let token;
    do {
        token = prompt("请输入：");
    } while (token.trim().length === 0)
    // 加密
    token = encrypt.encrypt(token);
    // 保存到会话域
    sessionStorage.setItem("token", token);
    // 保存到Cookie
    Cookies.set("token", token);
    return token;
}

/* 其他依赖js */
// RSA加密解密
document.write('<script src="/webjars/jsencrypt/bin/jsencrypt.js"></script>');
document.write('<script src="/js/utils/rsa.js"></script>');
// 操作Cookie
document.write('<script src="/webjars/js-cookie/dist/js.cookie.js"></script>');
