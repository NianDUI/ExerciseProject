const $ = layui.$;
const layer = layui.layer;
const form = layui.form;
const thisIndex = parent.layer.getFrameIndex(window.name);
parent.layer.iframeAuto(thisIndex);
const clientHeight = parent.document.documentElement.clientHeight;
const height = parent.layui.$("#layui-layer" + thisIndex).css("height").replace("px", "");
const style = {};
if (height >= clientHeight) {
    style.height = clientHeight + "px";
    style.top = 0 + "px";
} else {
    style.top = (clientHeight - height) / 2 + "px";
}
parent.layer.style(thisIndex, style);
// parent.layer.full(thisIndex);
if (nextid != null) {
    $(".prev").click(function () {
        parent.layer.iframeSrc(thisIndex, base + "chapter/show/" + previd)
    });
    $(".next").click(function () {
        parent.layer.iframeSrc(thisIndex, base + "chapter/show/" + nextid)
    });
}