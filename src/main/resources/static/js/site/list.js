// 参数设置
setBaseParams({
    idName: "siteid"
    , addUrl: base + "site/add/"
    , showBookUrl: base + "book/list/"
    , configAddUrl: base + "config/add/"
    , listUrl: base + "api/querySiteList"
    , deleteUrl: base + "api/deleteSite/"
});
const tableList = table.render({
    elem: "#table"
    , id: "table"
    , method: "post"
    , contentType: getBaseParams("contentType")
    , url: getBaseParams("listUrl")
    , where: {
        name: ""
    }
    , headers: headers()
    , request: tableRequest
    , response: tableReponse
    , parseData: tableParseData
    , page: true
    , height: tableHeight
    , text: tableText
    , cols: [[
        {checkbox: true, fixed: "left"}
        , {field: "name", title: "名称", minWidth: 180, event: "edit"}
        , {field: "configname", title: "配置", minWidth: 180, event: "configAdd"}
        , {field: "createtime", title: "创建时间", sort: true, minWidth: 180}
        , {field: "url", title: "链接", minWidth: 250}
        , {title: "操作", fixed: "right", minWidth: 180, align: "center", toolbar: "#toolbar"}
    ]]
});
table.on("tool(table)", function (obj) {
    var data = obj.data;
    if (obj.event === "showBook") {
        self.location.href = getBaseParams("showBookUrl", data.siteid);
    } else if (obj.event === "edit") {
        show(getBaseParams("addUrl", data.siteid));
    } else if (obj.event === "del") {
        layer.confirm("确认删除该信息？", function (index) {
            del(data.siteid);
            layer.close(index);
        });
    } else if (obj.event === "configAdd") {
        show(getBaseParams("configAddUrl", data.configid), "配置");
    }
});
list();