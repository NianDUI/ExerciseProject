setAddIframeStyle();
const thisIndex = baseParams.thisIndex;
if (id != null) {
    parent.layer.title("修改", thisIndex);
    $.ajax({
        type: "get"
        , contentType: "application/json"
        , url: base + "api/modelBook/" + id
        , success: function (data) {
            if (data.code == 200) {
                data = data.data;
                let handlerinfo = JSON.parse(data.handlerinfo);
                $(".reset").click(function () {
                    form.val("form", data);
                    form.val("form", handlerinfo);
                    setTitleShowHide();
                    setEndShowHide();
                    return false;
                }).click();
            } else {
                layer.alert(data.message);
            }
        }
    });
}
const titleType = $(".titleType");
const endType = $(".endType");
form.verify({
    startIndex: function (value) {
        if (titleType.val() != 0) {
            if (isNaN(value.trim())) {
                return "请输入数字";
            } else if (value < 0) {
                return "处理起始索引必须>=0";
            }
        }
    }
    , delimiter: function (value) {
        if (titleType.val() == 1) {
            if (value.length == 0) {
                return "请输入分隔符";
            }
        }
    }
    , endCharacter: function (value) {
        if (endType.val() == 1) {
            if (value.trim().length == 0) {
                return "请输入结束符";
            }
        }
    }
});
form.on("submit(submit)", function (data) {
    const field = data.field;
    field.bookid = id;
    field.handlerinfo = JSON.stringify({
        titleType: parseInt(field.titleType)
        , startIndex: parseInt(field.startIndex)
        , delimiter: field.delimiter
        , endType: parseInt(field.endType)
        , endCharacter: field.endCharacter
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
form.on('select(titleType)', function (data) {
    setTitleShowHide();
});
form.on('select(endType)', function (data) {
    setEndShowHide();
});
const startIndexDiv = $(".startIndexDiv");
const delimiterDiv = $(".delimiterDiv");
const endCharacterDiv = $(".endCharacterDiv");

function setTitleShowHide() {
    let val = titleType.val();
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

function setEndShowHide() {
    let val = endType.val();
    if (val == 1) {
        endCharacterDiv.show();
    } else {
        endCharacterDiv.hide();
    }
}