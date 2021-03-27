/* RSA加密解密相关 */
// document.write('<script src="/webjars/jsencrypt/bin/jsencrypt.js"></script>');
// 公钥
const PUBLIC_KEY = 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJg2Zsf4SrrGAJMSrfsjNJqyk9KxXNqcZUGttaIp/rsBJYyXidXR9r5NbYZ5ahqmNjSlWWxYmMfkPpe5nUFQyxsCAwEAAQ==';
// 私钥
const PRIVATE_KEY = '';

// 加密对象
const encrypt = new JSEncrypt();
//encrypt.setPrivateKey('-----BEGIN RSA PRIVATE KEY-----'+PRIVATE_KEY+'-----END RSA PRIVATE KEY-----');
encrypt.setPublicKey('-----BEGIN PUBLIC KEY-----' + PUBLIC_KEY + '-----END PUBLIC KEY-----');
/*var encrypted = encrypt.encrypt('ceshi01');
console.log('加密后数据:%o', encrypted);*/
// 解密对象
const decrypt = new JSEncrypt();
decrypt.setPublicKey('-----BEGIN PUBLIC KEY-----' + PUBLIC_KEY + '-----END PUBLIC KEY-----');
// decrypt.setPrivateKey('-----BEGIN RSA PRIVATE KEY-----'+PRIVATE_KEY+'-----END RSA PRIVATE KEY-----');
/*var uncrypted = decrypt.decrypt(encrypted);
console.log('解密后数据:%o', uncrypted);*/

/* 相关方法 */

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