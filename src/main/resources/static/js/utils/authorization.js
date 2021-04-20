/* 授权相关方法 */

// 获取请求头
function headers() {
    return {"Authorization": getAuthorization()};
}

// 处理Url token
function handleUrlAuthorization(authorization) {
    return authorization.replace("+", "%2B");
}

// 获取授权信息
function getAuthorization() {
    let authorization = Cookies.get("Authorization");
    if (authorization == null || authorization.length === 0) {
        authorization = sessionStorage.getItem("Authorization");
        if (authorization == null || authorization.length === 0) {
            // 设置授权信息
            authorization = setAuthorization();
        } else {
            Cookies.set("Authorization", authorization);
        }
    }
    return authorization;
}

// 设置授权信息
function setAuthorization() {
    let token;
    do {
        token = prompt("请输入：");
    } while (token.trim().length === 0)
    // 加密
    // token = encrypt.encrypt(token);
    // 授权信息: 组装并加密
    const authorization = encrypt.encrypt(JSON.stringify({token: token, buildTime : Date.now()}));
    // 保存到会话域
    sessionStorage.setItem("Authorization", authorization);
    // 保存到Cookie, 保存1天。https://blog.csdn.net/qq_20802379/article/details/81436634
    Cookies.set("Authorization", authorization, {expires: 1});
    return authorization;
}

/* 其他依赖js */
// RSA加密解密
document.write('<script src="/webjars/jsencrypt/bin/jsencrypt.js"></script>');
document.write('<script src="/js/utils/rsa.js"></script>');
// 操作Cookie
document.write('<script src="/webjars/js-cookie/dist/js.cookie.js"></script>');
