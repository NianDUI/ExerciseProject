// 参数设置
setBaseParams({
    showUrl: base + "chapter/show/"
});
setAddIframeStyle();
if (nextid != null) {
    const thisIndex = baseParams.thisIndex;
    $(".prev").click(function () {
        parent.layer.iframeSrc(thisIndex, getBaseParams("showUrl", previd))
    });
    $(".next").click(function () {
        parent.layer.iframeSrc(thisIndex, getBaseParams("showUrl", nextid))
    });
}