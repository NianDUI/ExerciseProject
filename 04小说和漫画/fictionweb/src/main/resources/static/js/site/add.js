// 参数设置
setBaseParams({
    saveUrl: base + "api/saveSite"
    , modelUrl: base + "api/modelSite/"
});
setAddIframeStyle();
const thisIndex = getBaseParams("thisIndex");
if (id != null) {
    parent.layer.title(getBaseParams("title"), thisIndex);
    $.ajax({
        type: "get"
        , contentType: getBaseParams("contentType")
        , url: getBaseParams("modelUrl", id)
        , success: function (data) {
            if (data.code === 200) {
                $(".reset").click(function () {
                    form.val("form", data.data);
                    return false;
                }).click();
            } else {
                layer.alert(data.message);
            }
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
    field.siteid = id;
    $.ajax({
        type: "post"
        , contentType: getBaseParams("contentType")
        , url: getBaseParams("saveUrl")
        , data: JSON.stringify(field)
        , success: function (data) {
            if (data.code === 200) {
                layer.msg("提交成功");
                parent.search();
                parent.layer.close(thisIndex);
            } else {
                layer.alert(data.message, {
                    title: "错误信息"
                });
            }
        }
    });
    return false;
});
