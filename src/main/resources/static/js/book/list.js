// 参数设置
setBaseParams({
    idName: "bookid"
    , addUrl: base + "book/add/"
    , showChapterUrl: base + "chapter/list/"
    , siteAddUrl: base + "site/add/"
    , configAddUrl: base + "config/add/"
    , listUrl: base + "api/queryBookList"
    , deleteUrl: base + "api/deleteBook/"
    , downloadUrl: base + "api/downloadBook/"
    , downloadUrl2: base + "api/downloadBook2/"
});
const tableList = table.render({
    elem: "#table"
    , id: "table"
    , method: "post"
    , contentType: getBaseParams("contentType")
    , url: getBaseParams("listUrl")
    , where: {
        name: ""
        , siteid: siteid
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
        , {field: "name", title: "名称", minWidth: 180}
        , {field: "sitename", title: "站点", minWidth: 180, event: "siteAdd"}
        , {field: "configname", title: "配置", minWidth: 180, event: "configAdd"}
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
        self.location.href = getBaseParams("showChapterUrl", data.bookid);
    } else if (obj.event === "download") {
        layer.msg("数据整理中请稍等！");
        location.href = getBaseParams("downloadUrl", data.bookid) + "?token=" + getToken();
    } else if (obj.event === "download2") {
        layer.msg("数据整理中请稍等！");
        location.href = getBaseParams("downloadUrl2", data.bookid) + "?token=" + getToken();
    } else if (obj.event === "edit") {
        show(getBaseParams("addUrl", data.bookid));
    } else if (obj.event === "del") {
        layer.confirm("确认删除该信息？", function (index) {
            del(data.bookid);
            layer.close(index);
        });
    } else if (obj.event === "siteAdd") {
        show(getBaseParams("siteAddUrl", data.siteid), "站点");
    } else if (obj.event === "configAdd") {
        show(getBaseParams("configAddUrl", data.configid), "配置");
    }
});
list();