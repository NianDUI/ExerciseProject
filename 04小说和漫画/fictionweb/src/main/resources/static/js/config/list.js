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
    , request: {pageName: "pageNum", limitName: "pageSize"}
    , response: {statusCode: 200}
    , parseData: function (res) {
        return {"code": res.code, "msg": res.message, "count": res.data.total, "data": res.data.list}
    }
    , page: true
    , text: "获取失败"
    , height: "full-110"
    , text: {none: "暂无相关数据"}
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
        , {title: "操作", fixed: "right", minWidth: 100, align: "center", toolbar: "#toolbar"}
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
$(".search").click(function () {
    search();
});
$(".add").click(function () {
    add("null");
});
$(".delAll").click(function () {
    const data = table.checkStatus("table").data;
    if (data.length == 0) {
        layer.msg("请选择要删除的信息");
    } else {
        layer.confirm("确认删除已选信息？", function (index) {
            let id = "" + data[0].configid;
            for (let i = 1; i < data.length; i++) {
                id += "," + data[i].configid;
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
        , area: "450px"
        , offset: "20px"
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