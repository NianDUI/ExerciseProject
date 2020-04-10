const table = layui.table;
const $ = layui.$;
const layer = layui.layer;
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
        , {field: "name", title: "名称"}
        , {field: "rawname", title: "原名称"}
        , {field: "bookname", title: "书籍"}
        , {field: "configname", title: "配置"}
        , {field: "createtime", title: "创建时间"}
        , {field: "url", title: "链接"}
        , {title: "操作", fixed: "right", minWidth: 208, align: "center", toolbar: "#toolbar"}
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
list();
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

function search() {
    tableList.reload({
        page: {curr: 1}, where: {name: $(".searVal").val()}
    })
}

function add(id) {
    layer.open({
        type: 2
        , title: "添加"
        , shadeClose: true
        , maxmin: true
        , area: computeArea() + "px"
        // , offset: "t"
        , content: base + "chapter/add/" + id // [, "no"]
    });
}

function del(id) {
    $.ajax({
        type: "get"
        , contentType: "application/json"
        , url: base + "api/deleteChapter/" + id
        , success: function (data) {
            if (data.code == 200) {
                layer.msg("删除成功");
                search();
            } else {
                layer.alert(data.message);
            }
        }
    });
}