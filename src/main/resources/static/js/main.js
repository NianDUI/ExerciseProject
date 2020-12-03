const $ = layui.$;
$(".book").click(function () {
    self.location.href = bookPath;
});
$(".site").click(function () {
    self.location.href = sitePath;
});
$(".config").click(function () {
    self.location.href = configPath;
});