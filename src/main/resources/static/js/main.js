/* const $ = layui.$;
$(".book").click(function () {
    self.location.href = bookPath;
});
$(".site").click(function () {
    self.location.href = sitePath;
});
$(".config").click(function () {
    self.location.href = configPath;
});*/

/*const file = $(".file");
const fileHref = file.attr("href");
let token = sessionStorage.getItem("token");
if (token != null && token.length > 0) {
    // token存在,更新文件列表链接
    file.attr("href", fileHref + "?token=" + handleUrlToken(token));
    // 特殊字符串处理：https://blog.csdn.net/weixin_37865166/article/details/104572737
}
    // // 更新文件列表链接
    // file.attr("href", fileHref + "?token=" + token);
*/
// 获取授权
$(".file").click(getAuthorization);
// 设置授权
$(".setAuthorization").click(setAuthorization);