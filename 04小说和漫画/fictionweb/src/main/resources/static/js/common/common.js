// list
const tableRequest = {pageName: "pageNum", limitName: "pageSize"};
const tableReponse = {statusCode: 200};
const tableText = {none: "暂无相关数据"};
const tableHeight = "full-145";

function tableParseData(res) {
    return {"code": res.code, "msg": res.message, "count": res.data.total, "data": res.data.list}
}

function list(idName) {
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


// add
function getIframeStyle() {
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
