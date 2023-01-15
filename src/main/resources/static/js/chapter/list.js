// 参数设置
setBaseParams({
    idName: "chapterid"
    , addUrl: base + "chapter/add/"
    , showUrl: base + "chapter/show/"
    , bookAddUrl: base + "book/add/"
    , configAddUrl: base + "config/add/"
    , listUrl: base + "api/queryChapterList"
    , deleteUrl: base + "api/deleteChapter/"
    , reacquireSingleChapterUrl: base + "api/reacquireSingleChapter/"
    , statusUrl: base + "api/queryGetStatus/"
    , stopUrl: base + "api/stopGet/"
    , reacquireAllChapterUrl: base + "api/reacquireAllChapter/"
    , getFollowUpChapterUrl: base + "api/getFollowUpChapter/"
    , getSpecifiedAndFollowUpChapterUrl: base + "api/getSpecifiedAndFollowUpChapter"
});
const tableList = table.render({
    elem: "#table"
    , id: "table"
    , method: "post"
    , contentType: getBaseParams("contentType")
    , url: getBaseParams("listUrl")
    , where: {
        name: ""
        , bookid: bookid
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
        , {field: "name", title: "名称", minWidth: 200, event: "edit"}
        , {field: "rawname", title: "原名称", minWidth: 200, event: "edit"}
        , {field: "bookname", title: "书籍", minWidth: 180, event: "bookAdd"}
        , {field: "configname", title: "配置", minWidth: 180, event: "configAdd"}
        , {field: "createtime", title: "创建时间", sort: true, minWidth: 180}
        , {field: "url", title: "链接", minWidth: 300}
        , {title: "操作", fixed: "right", minWidth: 230, align: "center", toolbar: "#toolbar"}
    ]]
});
table.on("tool(table)", function (obj) {
    var data = obj.data;
    if (obj.event === "edit") {
        show(getBaseParams("addUrl", data.chapterid));
    } else if (obj.event === "del") {
        layer.confirm("确认删除该信息？", function (index) {
            del(data.chapterid);
            layer.close(index);
        });
    } else if (obj.event === "show") {
        show(getBaseParams("showUrl", data.chapterid), "查看")
    } else if (obj.event === "reacquire") {
        const index = layer.load(2);
        ajax({
            type: "get"
            , contentType: getBaseParams("contentType")
            , url: getBaseParams("reacquireSingleChapterUrl", data.chapterid)
            , successBefore: function (data) {
                layer.close(index);
            }
            , success: function (data) {
                layer.msg("获取成功");
            }
        });
    } else if (obj.event === "bookAdd") {
        show(getBaseParams("bookAddUrl", data.bookid), "书籍");
    } else if (obj.event === "configAdd") {
        show(getBaseParams("configAddUrl", data.configid), "配置");
    }
});
list();

if (taskswitch !== 0) {
    const stopGet = $(".stopGet");
    let timer = null;
    // 是否自动开启定时刷新任务
    if (taskstatus !== 0) {
        schedule();
    }

    // 启用定时刷新任务
    function schedule() {
        if (timer == null) {
            stopGet.show();
            // timer = window.setInterval("pageNum = 0x7fffffff - 20;searchBtn.click();", 5000);
            timer = window.setInterval("pageNum = 9999999;searchBtn.click();", 5000);
        }
    }

    // 停止定时刷新任务
    function cancel() {
        if (timer != null) {
            stopGet.hide();
            window.clearInterval(timer);
            timer = null;
            taskstatus = 0;
        }
    }

    searchBtn.click(function () {
        ajax({
            type: "get"
            , contentType: getBaseParams("contentType")
            , url: getBaseParams("statusUrl", bookid)
            , success: function (data) {
                if (data.data !== 0) {
                    taskstatus = data.data;
                    schedule();
                } else {
                    cancel();
                }
            }
        });
    });
    stopGet.click(function () {
        ajax({
            type: "get"
            , contentType: getBaseParams("contentType")
            , url: getBaseParams("stopUrl", bookid)
            , success: function (data) {
                layer.msg("停止获取");
                cancel();
            }
        });
    });
    $(".reacquire").click(function () {
        ajax({
            type: "get"
            , contentType: getBaseParams("contentType")
            , url: getBaseParams("reacquireAllChapterUrl", bookid)
            , success: function (data) {
                layer.msg("正在获取");
                schedule();
            }
        });
    });
    $(".getFollowUp").click(function () {
        ajax({
            type: "get"
            , contentType: getBaseParams("contentType")
            , url: getBaseParams("getFollowUpChapterUrl", bookid)
            , success: function (data) {
                layer.msg("正在获取");
                schedule();
            }
        });
    });
    $(".getSpecifiedAndFollowUp").click(function () {
        layer.prompt({
            formType: 2
            , value: '' //初始值
            , maxlength: 128
            , title: '请输入章节链接'
            , area: [computeArea() + "px", "25px"]
        }, function (value, index, elem) {
            if (value.trim().length > 0) {
                ajax({
                    type: "post"
                    , contentType: getBaseParams("contentType")
                    , url: getBaseParams("getSpecifiedAndFollowUpChapterUrl")
                    , data: JSON.stringify({bookid: bookid, url: value})
                    , success: function (data) {
                        layer.msg("正在获取");
                        schedule();
                        layer.close(index);
                    }
                });
            } else {
                layer.msg("请输入章节连接", {time: 1000});
            }
        });
    });
}