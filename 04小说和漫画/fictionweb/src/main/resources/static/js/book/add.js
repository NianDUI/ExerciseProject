const $ = layui.$;
const layer = layui.layer;
const form = layui.form;
const thisIndex = parent.layer.getFrameIndex(window.name);
parent.layer.iframeAuto(thisIndex);
const clientHeight = parent.document.documentElement.clientHeight;
const height = parent.layui.$("#layui-layer" + thisIndex).css("height").replace("px", "");
const style = {};
if (height >= clientHeight) {
    style.height = clientHeight + "px";
    style.top = 0 + "px";
} else {
    style.top = (clientHeight - height) / 2 + "px";
}
parent.layer.style(thisIndex, style);
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