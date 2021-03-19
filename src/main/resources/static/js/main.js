const $ = layui.$;
/*$(".book").click(function () {
    self.location.href = bookPath;
});
$(".site").click(function () {
    self.location.href = sitePath;
});
$(".config").click(function () {
    self.location.href = configPath;
});*/

const file = $(".file");
const fileHref = file.attr("href");
let token = sessionStorage.getItem("token");
if (token != null && token.length > 0) {
    // token存在,更新文件列表链接
    file.attr("href", fileHref + "?token=" + token.replace("+", "%2B"));
}
// 设置token
$(".setToken").click(function () {
    do {
        token = prompt("请输入：");
    } while (token.trim().length === 0)
    // 加密
    token = encrypt.encrypt(token);
    sessionStorage.setItem("token", token);
    // 更新文件列表链接
    file.attr("href", fileHref + "?token=" + token);
});