// 参数设置
setBaseParams({
    saveUrl: base + "api/saveChapter"
    , modelUrl: base + "api/modelChapter/"
});
setAddIframeStyle();
const thisIndex = getBaseParams("thisIndex");
if (id != null) {
    parent.layer.title(getBaseParams("title"), thisIndex);
    ajax({
        type: "get"
        , contentType: getBaseParams("contentType")
        , url: getBaseParams("modelUrl", id)
        , success: function (data) {
            $(".reset").click(function () {
                form.val("form", data.data);
                return false;
            }).click();
        }
    });
}
form.verify({
    configid: function (value) {
        value = value.trim();
        if (isNaN(value)) {
            return "请输入数字 ";
        }
    }
});
form.on("submit(submit)", function (data) {
    const field = data.field;
    field.chapterid = id;
    ajax({
        type: "post"
        , contentType: getBaseParams("contentType")
        , url: getBaseParams("saveUrl")
        , data: JSON.stringify(field)
        , success: function (data) {
            layer.msg("提交成功");
            parent.search();
            parent.layer.close(thisIndex);
        }
    });
    return false;
});
