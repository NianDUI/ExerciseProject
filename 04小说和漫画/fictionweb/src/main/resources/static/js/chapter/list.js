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
    , request: {pageName: "pageNum", limitName: "pageSize"}
    , response: {statusCode: 200}
    , parseData: function (res) {
        return {"code": res.code, "msg": res.message, "count": res.data.total, "data": res.data.list}
    }
    , page: true
    , text: "获取失败"
    , height: "full-145"
    , text: {none: "暂无相关数据"}
    , cols: [[
        {checkbox: true, fixed: "left"}
        , {field: "name", title: "名称"}
        , {field: "rawname", title: "原名称"}
        , {field: "bookname", title: "书籍"}
        , {field: "configname", title: "配置"}
        , {field: "createtime", title: "创建时间"}
        , {field: "url", title: "链接"}
        , {title: "操作", fixed: "right", minWidth: 160, align: "center", toolbar: "#toolbar"}
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
    } else if (obj.event === "edit") {
        add(data.chapterid);
    } else if (obj.event === "del") {
        layer.confirm("确认删除该信息？", function (index) {
            del(data.chapterid);
            layer.close(index);
        });
    }
});
$(".search").click(function () {
    search();
});
$(".reacquire").click(function () {
    $.ajax({
        type: "get"
        , contentType: "application/json"
        , url: base + "api/reacquireChapter/" + bookid
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
$(".delAll").click(function () {
    const data = table.checkStatus("table").data;
    if (data.length == 0) {
        layer.msg("请选择要删除的信息");
    } else {
        layer.confirm("确认删除已选信息？", function (index) {
            let id = "" + data[0].chapterid;
            for (let i = 1; i < data.length; i++) {
                id += "," + data[i].chapterid;
            }
            del(id);
            layer.close(index);
        });
    }
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

function computeArea() {
    let area = document.documentElement.clientWidth / 5 * 3;
    if (area < 400) {
        if (document.documentElement.clientWidth < 400) {
            area = document.documentElement.clientWidth;
        } else {
            area = 400;
        }
    }
    return area;
}