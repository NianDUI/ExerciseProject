const $ = layui.$;
const layer = layui.layer;
const form = layui.form;
const thisIndex = parent.layer.getFrameIndex(window.name);
parent.layer.iframeAuto(thisIndex);
parent.layer.style(thisIndex, getIframeStyle());
// parent.layer.full(thisIndex);
if (id != null) {
    parent.layer.title("修改", thisIndex);
    $.ajax({
        type: "get"
        , contentType: "application/json"
        , url: base + "api/modelBook/" + id
        , success: function (data) {
            if (data.code == 200) {
                data = data.data;
                let titlehandler = JSON.parse(data.titlehandler);
                $(".reset").click(function () {
                    form.val("form", data);
                    form.val("form", titlehandler);
                    setShowHide();
                    return false;
                }).click();
            } else {
                layer.alert(data.message);
            }
        }
    });
}
const handleType = $(".handleType");
form.verify({
    startIndex: function (value) {
        if (handleType.val() != 0) {
            if (isNaN(value.trim())) {
                return "请输入数字";
            } else if (value < 0) {
                return "处理起始索引必须>=0";
            }
        }
    }
    , delimiter: function (value) {
        if (handleType.val() == 1) {
            if (value.length == 0) {
                return "请输入分隔符";
            }
        }
    }
});
form.on("submit(submit)", function (data) {
    const field = data.field;
    field.bookid = id;
    field.titlehandler = JSON.stringify({
        handleType: parseInt(field.handleType)
        , startIndex: parseInt(field.startIndex)
        , delimiter: field.delimiter
    });
    $.ajax({
        type: "post"
        , contentType: "application/json"
        , url: base + "api/saveBook"
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
form.on('select(siteid)', function (data) {
    if (data.value.trim().length == 0) {
        return;
    }
    $.ajax({
        type: "get"
        , contentType: "application/json"
        , url: base + "api/modelSite/" + data.value
        , success: function (data) {
            if (data.code == 200) {
                form.val("form", {configid: data.data.configid});
            } else {
                layer.alert(data.message);
            }
        }
    });
});
form.on('select(handleType)', function (data) {
    setShowHide();
});
const startIndexDiv = $(".startIndexDiv");
const delimiterDiv = $(".delimiterDiv");

function setShowHide() {
    let val = handleType.val();
    if (val == 1) {
        startIndexDiv.show();
        delimiterDiv.show();
    } else if (val == 2) {
        startIndexDiv.show();
        delimiterDiv.hide();
    } else {
        startIndexDiv.hide();
        delimiterDiv.hide();
    }
}