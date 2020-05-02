const table = layui.table;
const $ = layui.$;
const layer = layui.layer;
const tableList = table.render({
    elem: "#table"
    , id: "table"
    , method: "post"
    , contentType: "application/json"
    , url: base + "api/queryBookList"
    , where: {
        name: ""
        , siteid: siteid
    }
    , request: tableRequest
    , response: tableReponse
    , parseData: tableParseData
    , page: true
    , height: tableHeight
    , text: tableText
    , cols: [[
        {checkbox: true, fixed: "left"}
        , {field: "name", title: "名称", minWidth: 180}
        , {field: "sitename", title: "站点", minWidth: 180}
        , {field: "configname", title: "配置", minWidth: 180}
        , {field: "taskname", title: "任务"}
        , {field: "createtime", title: "创建时间", sort: true, minWidth: 180}
        , {field: "url", title: "链接", minWidth: 250}
        , {field: "starturl", title: "起始章节链接", minWidth: 300}
        , {title: "操作", fixed: "right", minWidth: 230, align: "center", toolbar: "#toolbar"}
    ]]
});
table.on("tool(table)", function (obj) {
    var data = obj.data;
    if (obj.event === "showChapter") {
        self.location.href = base + "chapter/list/" + data.bookid;
    } else if (obj.event === "download") {
        layer.msg("数据整理中请稍等！");
        location.href = base + "api/downloadBook/" + data.bookid;
    } else if (obj.event === "edit") {
        add(data.bookid);
    } else if (obj.event === "del") {
        layer.confirm("确认删除该信息？", function (index) {
            del(data.bookid);
            layer.close(index);
        });
    }
});
list();

function search() {
    tableList.reload({
        page: {curr: 1}, where: {name: $(".searVal").val(), siteid: siteid}
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
        , content: base + "book/add/" + id // [, "no"]
    });
}

function del(id) {
    $.ajax({
        type: "get"
        , contentType: "application/json"
        , url: base + "api/deleteBook/" + id
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