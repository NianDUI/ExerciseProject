/* 通用对象 */
const table = layui.table;
const $ = layui.$;
const layer = layui.layer;
const form = layui.form;
// 基本参数
let baseParams = {};
// 设置基本参数方法
function setBaseParams(params) {
    baseParams = params;
}

/* 列表页面相关 */
// 列表页面公用参数
const tableRequest = {pageName: "pageNum", limitName: "pageSize"};
const tableReponse = {statusCode: 200};
const tableText = {none: "暂无相关数据"};
const tableHeight = "full-145";
// 表格响应参数映射
function tableParseData(res) {
    return {"code": res.code, "msg": res.message, "count": res.data.total, "data": res.data.list}
}
// 列表页面需要响应
function list() {
    const idName = baseParams.idName;
    table.on("rowDouble(table)", function (obj) {
        add(obj.data[idName]);
    });
    $(".search").click(search);
    // 按键弹起事件
    $(".searVal").keyup(function (e) {
        // 回车
        if (e.keyCode == 13) {
            search();
        }
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
                let id = "" + data[0][idName];
                for (let i = 1; i < data.length; i++) {
                    id += "," + data[i][idName];
                }
                del(id);
                layer.close(index);
            });
        }
    });
}
// 列表页面搜索方法
let searVal = "";
function search() {
    const name = $(".searVal").val().trim();
    const params = {where: {name: name}};
    if (name != searVal) {
        params.page = {curr: 1};
        searVal = name;
    }
    tableList.reload(params);
}
// 列表页面跳转添加修改页面
function add(id) {
    layer.open({
        type: 2
        , title: "添加"
        , shadeClose: true
        , maxmin: true
        , area: computeArea() + "px"
        // , offset: "t"
        , content: baseParams.addUrl + id // [, "no"]
    });
}
// 列表页面删除方法
function del(id) {
    $.ajax({
        type: "get"
        , contentType: "application/json"
        , url: baseParams.delUrl + id
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
// 计算添加窗口的宽度
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

/* 添加页面相关 */
// 获取添加页面样式
function getIframeStyle(thisIndex) {
    const clientHeight = parent.document.documentElement.clientHeight;
    const height = parent.layui.$("#layui-layer" + thisIndex).css("height").replace("px", "");
    const style = {};
    if (height >= clientHeight) {
        style.height = clientHeight + "px";
        style.top = 0 + "px";
    } else {
        style.top = (clientHeight - height) / 2 + "px";
    }
    return style;
}
// 设置添加(本)页面的样式
function setAddIframeStyle() {
    const thisIndex = parent.layer.getFrameIndex(window.name);
    baseParams.thisIndex = thisIndex;
    parent.layer.iframeAuto(thisIndex);
    parent.layer.style(thisIndex, getIframeStyle(thisIndex));
    // parent.layer.full(thisIndex);
}
