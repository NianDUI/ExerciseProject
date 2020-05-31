setAddIframeStyle();
if (nextid != null) {
    const thisIndex = baseParams.thisIndex;
    $(".prev").click(function () {
        parent.layer.iframeSrc(thisIndex, base + "chapter/show/" + previd)
    });
    $(".next").click(function () {
        parent.layer.iframeSrc(thisIndex, base + "chapter/show/" + nextid)
    });
}