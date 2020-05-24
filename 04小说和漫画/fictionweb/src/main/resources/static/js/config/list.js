const table = layui.table;
const $ = layui.$;
const layer = layui.layer;
const tableList = table.render({
    elem: "#table"
    , id: "table"
    , method: "post"
    , contentType: "application/json"
    , url: base + "api/queryConfigList"
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
        , {field: "titlematch", title: "标题匹配", minWidth: 300}
        , {field: "titlelnnum", title: "标题后换行", sort: true}
        , {field: "conmatch", title: "内容匹配", minWidth: 250}
        , {field: "conlnnum", title: "内容后换行", sort: true}
        , {field: "startoffset", title: "开始偏移量", sort: true}
        , {field: "endoffset", title: "结束偏移量", sort: true}
        , {field: "amatch", title: "跳转匹配", minWidth: 280}
        , {field: "nexta", title: "下一页索引", sort: true}
        , {title: "操作", fixed: "right", minWidth: 120, align: "center", toolbar: "#toolbar"}
    ]]
});
table.on("tool(table)", function (obj) {
    var data = obj.data;
    if (obj.event === "edit") {
        add(data.configid);
    } else if (obj.event === "del") {
        layer.confirm("确认删除该信息？", function (index) {
            del(data.configid);
            layer.close(index);
        });
    }
});
list({
    idName: "configid"
    , addUrl: base + "config/add/"
    , delUrl: base + "api/deleteConfig/"
});