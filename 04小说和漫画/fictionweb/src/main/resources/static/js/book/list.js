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
        , {field: "taskstatusname", title: "任务状态", minWidth: 90}
        , {field: "taskswitchname", title: "任务开关", minWidth: 90}
        , {field: "createtime", title: "创建时间", sort: true, minWidth: 180}
        , {field: "url", title: "链接", minWidth: 250}
        , {field: "starturl", title: "起始章节链接", minWidth: 300}
        , {title: "操作", fixed: "right", minWidth: 290, align: "center", toolbar: "#toolbar"}
    ]]
});
table.on("tool(table)", function (obj) {
    var data = obj.data;
    if (obj.event === "showChapter") {
        self.location.href = base + "chapter/list/" + data.bookid;
    } else if (obj.event === "download") {
        layer.msg("数据整理中请稍等！");
        location.href = base + "api/downloadBook/" + data.bookid;
    } else if (obj.event === "download2") {
        location.href = base + "api/downloadBook2/" + data.bookid;
    } else if (obj.event === "edit") {
        add(data.bookid);
    } else if (obj.event === "del") {
        layer.confirm("确认删除该信息？", function (index) {
            del(data.bookid);
            layer.close(index);
        });
    }
});
setBaseParams({
    idName: "bookid"
    , addUrl: base + "book/add/"
    , delUrl: base + "api/deleteBook/"
});
list();