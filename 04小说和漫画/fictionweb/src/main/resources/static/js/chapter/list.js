const tableList = table.render({
    elem: "#table"
    , id: "table"
    , method: "post"
    , contentType: "application/json"
    , url: base + "api/queryChapterList"
    , where: {
        name: ""
        , bookid: bookid
    }
    , request: tableRequest
    , response: tableReponse
    , parseData: tableParseData
    , page: true
    , height: tableHeight
    , text: tableText
    , cols: [[
        {checkbox: true, fixed: "left"}
        , {field: "name", title: "名称", minWidth: 200}
        , {field: "rawname", title: "原名称", minWidth: 200}
        , {field: "bookname", title: "书籍", minWidth: 180}
        , {field: "configname", title: "配置", minWidth: 180}
        , {field: "createtime", title: "创建时间", sort: true, minWidth: 180}
        , {field: "url", title: "链接", minWidth: 300}
        , {title: "操作", fixed: "right", minWidth: 230, align: "center", toolbar: "#toolbar"}
    ]]
});
table.on("tool(table)", function (obj) {
    var data = obj.data;
    if (obj.event === "show") {
        layer.open({
            type: 2
            , title: "查看"
            , shadeClose: true
            , maxmin: true
            , area: computeArea() + "px"
            // , offset: "t"
            , content: base + "chapter/show/" + data.chapterid // [, "no"]
        })
        ;
    } else if (obj.event === "reacquire") {
        const index = layer.load(2);
        $.ajax({
            type: "get"
            , contentType: "application/json"
            , url: base + "api/reacquireSingleChapter/" + data.chapterid
            , success: function (data) {
                layer.close(index);
                if (data.code == 200) {
                    layer.msg("获取成功");
                } else {
                    layer.alert(data.message);
                }
            }
        });
    } else if (obj.event === "edit") {
        add(data.chapterid);
    } else if (obj.event === "del") {
        layer.confirm("确认删除该信息？", function (index) {
            del(data.chapterid);
            layer.close(index);
        });
    }
});
setBaseParams({
    idName: "chapterid"
    , addUrl: base + "chapter/add/"
    , delUrl: base + "api/deleteChapter/"
});
list();

const stopGet = $(".stopGet");
function queryGetStatus() {
    $.ajax({
        type: "get"
        , contentType: "application/json"
        , url: base + "api/queryGetStatus/" + bookid
        , success: function (data) {
            if (data.code == 200) {
                if (data.data) {
                    stopGet.show();
                } else {
                    stopGet.hide();
                }
            } else {
                layer.alert(data.message);
            }
        }
    });
}
$(".search").click(queryGetStatus);
stopGet.click(function () {
    $.ajax({
        type: "get"
        , contentType: "application/json"
        , url: base + "api/stopGet/" + bookid
        , success: function (data) {
            if (data.code == 200) {
                layer.msg("停止获取");
            } else {
                layer.alert(data.message);
            }
        }
    });
});
$(".reacquire").click(function () {
    $.ajax({
        type: "get"
        , contentType: "application/json"
        , url: base + "api/reacquireAllChapter/" + bookid
        , success: function (data) {
            if (data.code == 200) {
                layer.msg("正在获取");
            } else {
                layer.alert(data.message);
            }
        }
    });
});
$(".getFollowUp").click(function () {
    $.ajax({
        type: "get"
        , contentType: "application/json"
        , url: base + "api/getFollowUpChapter/" + bookid
        , success: function (data) {
            if (data.code == 200) {
                layer.msg("正在获取");
            } else {
                layer.alert(data.message);
            }
        }
    });
});
$(".getSpecifiedAndFollowUp").click(function () {
    layer.prompt({
        formType: 2
        , value: '' //初始值
        , maxlength: 128
        , title: '请输入章节链接'
        , area: [computeArea() + "px", "25px"]
    }, function (value, index, elem) {
        if (value.trim().length > 0) {
            $.ajax({
                type: "post"
                , contentType: "application/json"
                , url: base + "api/getSpecifiedAndFollowUpChapter"
                , data: JSON.stringify({bookid: bookid, url: value})
                , success: function (data) {
                    if (data.code == 200) {
                        layer.msg("正在获取");
                        layer.close(index);
                    } else {
                        layer.alert(data.message);
                    }
                }
            });
        } else {
            layer.msg("请输入章节连接", {time: 1000});
        }
    });
});