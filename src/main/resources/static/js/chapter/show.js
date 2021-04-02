// 参数设置
setBaseParams({
    showUrl: base + "chapter/show/"
});
setAddIframeStyle();
const thisIndex = baseParams.thisIndex;
if (previd != null) {
    $(".prev").click(function () {
        parent.layer.iframeSrc(thisIndex, getBaseParams("showUrl", previd))
    });
}
if (nextid != null) {
    $(".next").click(function () {
        parent.layer.iframeSrc(thisIndex, getBaseParams("showUrl", nextid))
    });
}