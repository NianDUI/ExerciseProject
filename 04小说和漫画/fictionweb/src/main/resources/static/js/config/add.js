const $ = layui.$;
const layer = layui.layer;
const form = layui.form;
const thisIndex = parent.layer.getFrameIndex(window.name);
parent.layer.iframeAuto(thisIndex);
parent.layer.full(thisIndex);
if (id != null) {
    parent.layer.title("修改", thisIndex);
    $.ajax({
        type: "get"
        , contentType: "application/json"
        , url: base + "api/modelConfig/" + id
        , success: function (data) {
            if (data.code == 200) {
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
form.render();
form.verify({
    titlelnnum: function (value) {
        if (value < 0) {
            return "标题后换行数量必须>=0";
        }
    }
    , conlnnum: function (value) {
        if (value < 0) {
            return "内容后换行数量必须>=0";
        }
    }
    , startoffset: function (value) {
        if (value < 0) {
            return "内容开始索引偏移量必须>=0";
        }
    }
    , endoffset: function (value) {
        if (value > 0) {
            return "内容结束索引偏移量必须<=0";
        }
    }
    , nexta: function (value) {
        if (value < 0) {
            return "下一页超链接索引必须>=0";
        }
    }
});
form.on("submit(submit)", function (data) {
    const field = data.field;
    field.configid = id;
    $.ajax({
        type: "post"
        , contentType: "application/json"
        , url: base + "api/saveConfig"
        , data: JSON.stringify(field)
        , success: function (data) {
            if (data.code == 200) {
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
