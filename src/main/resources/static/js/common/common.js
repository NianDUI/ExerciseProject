/* 通用对象 */
const table = layui.table;
const $ = layui.$;
const layer = layui.layer;
const form = layui.form;
// 基本参数
let baseParams = {
    contentType: "application/json"
};

// 设置基本参数方法
function setBaseParams(key, value) {
    if (key == null) {
        return;
    }
    if (typeof key === "object") {
        for (let keyKey in key) {
            if (key.hasOwnProperty(keyKey)) {
                baseParams[keyKey] = key[keyKey];
            }
        }
    } else {
        baseParams[key] = value;
    }
}

// 获取基本参数方法
function getBaseParams(key, end) {
    if (key === "baseParams") {
        return baseParams;
    }
    if (end == null) {
        return baseParams[key];
    } else {
        return baseParams[key] + end;
    }
}

// ajax请求
function ajax(options) {
    if (options.headers == null) {
        options.headers = headers();
    } else {
        options.headers.Token = getToken();
    }
    let success = options.success;
    if (typeof success === "function") {
        options.success = function (data) {
            let successBefore = options.successBefore;
            if (typeof successBefore === "function") {
                successBefore(data);
            }
            if (data.code === 200) {
                success(data);
            } else {
                layer.alert(data.message, {
                    title: "错误信息"
                });
            }
        }
    }
    $.ajax(options);
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
const searchBtn = $(".search");
const searchInput = $(".searVal");

function list() {
    const idName = getBaseParams("idName");
    // 表格行双击事件
    table.on("rowDouble(table)", function (obj) {
        show(getBaseParams("addUrl", obj.data[idName]));
    });
    // 搜索点击事件
    searchBtn.click(search);
    // 按键弹起事件
    searchInput.keyup(function (e) {
        // 回车
        if (e.keyCode === 13) {
            search();
        }
    });
    $(".add").click(function () {
        show(getBaseParams("addUrl", "null"));
    });
    $(".delAll").click(function () {
        const data = table.checkStatus("table").data;
        if (data.length === 0) {
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
let searVal = "", pageNum = 1;

function search() {
    const name = searchInput.val().trim();
    const params = {where: {name: name}, page: {}};
    if (name !== searVal) {
        params.page.curr = 1;
        searVal = name;
    }
    if (pageNum !== 1) {
        params.page.curr = pageNum;
        pageNum = 1;
    }
    // 表格重载：tableIns.reload(options, deep)
    // 参数 options 即为各项基础参数
    // 参数 deep：是否采用深度重载（即参数深度克隆，也就是重载时始终携带初始时及上一次重载时的参数），默认 false
    // 注意：deep 参数为 layui 2.6.0 开始新增。
    tableList.reload(params, true);
}

// 列表页面跳转添加修改页面
function show(url, title) {
    layer.open({
        type: 2
        , title: title == null ? "添加" : title
        , shadeClose: true
        , maxmin: true
        , area: computeArea() + "px"
        // , offset: "t"
        , content: url // [, "no"]
    });
}

// 列表页面删除方法
function del(id) {
    ajax({
        type: "get"
        , contentType: getBaseParams("contentType")
        , url: getBaseParams("deleteUrl", id)
        , success: function (data) {
            layer.msg("删除成功");
            search();
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
    setBaseParams("thisIndex", thisIndex);
    const titleRaw = parent.layui.$("#layui-layer" + thisIndex + " .layui-layer-title").html()
        // "title" 在 id != null 的时候被使用。不是修改、就是复制
        // , title = titleRaw === "添加" ? "修改" : titleRaw + "修改"; // 20230301 注释
        , title = titleRaw === "复制" ? "复制" : (titleRaw === "添加" ? "修改" : titleRaw + "修改");
    setBaseParams("title", title);
    parent.layer.iframeAuto(thisIndex);
    parent.layer.style(thisIndex, getIframeStyle(thisIndex));
    // parent.layer.full(thisIndex);
}


/* 其他依赖js */
// token相关方法
document.write('<script src="/js/utils/token.js"></script>');
