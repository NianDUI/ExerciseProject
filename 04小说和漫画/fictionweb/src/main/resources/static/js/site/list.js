const tableList = table.render({
    elem: "#table"
    , id: "table"
    , method: "post"
    , contentType: "application/json"
    , url: base + "api/querySiteList"
    , where: {
        name: ""
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
        , {field: "configname", title: "配置", minWidth: 180}
        , {field: "createtime", title: "创建时间", sort: true, minWidth: 180}
        , {field: "url", title: "链接", minWidth: 250}
        , {title: "操作", fixed: "right", minWidth: 180, align: "center", toolbar: "#toolbar"}
    ]]
});
table.on("tool(table)", function (obj) {
    var data = obj.data;
    if (obj.event === "showBook") {
        self.location.href = base + "book/list/" + data.siteid;
    } else if (obj.event === "edit") {
        add(data.siteid);
    } else if (obj.event === "del") {
        layer.confirm("确认删除该信息？", function (index) {
            del(data.siteid);
            layer.close(index);
        });
    }
});
setBaseParams({
    idName: "siteid"
    , addUrl: base + "site/add/"
    , delUrl: base + "api/deleteSite/"
});
list();