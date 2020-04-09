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
        , {field: "name", title: "名称"}
        , {field: "titlematch", title: "标题匹配"}
        , {field: "titlelnnum", title: "标题后换行"}
        , {field: "conmatch", title: "内容匹配"}
        , {field: "conlnnum", title: "内容后换行"}
        , {field: "startoffset", title: "开始偏移量"}
        , {field: "endoffset", title: "结束偏移量"}
        , {field: "amatch", title: "跳转匹配"}
        , {field: "nexta", title: "下一页索引"}
        , {title: "操作", fixed: "right", minWidth: 114, align: "center", toolbar: "#toolbar"}
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
list();

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
        , content: base + "config/add/" + id // [, "no"]
    });
}

function del(id) {
    $.ajax({
        type: "get"
        , contentType: "application/json"
        , url: base + "api/deleteConfig/" + id
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