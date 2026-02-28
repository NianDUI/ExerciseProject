import {encodePath} from "../../../modern/js/common/http.js";

export default {
    name: "MobileImageViewerPage",
    template: `
    <div class="image-viewer-page">
      <div class="video-player-topbar">
        <button class="ghost-btn" @click="$router.back()">返回</button>
        <div class="video-player-title">{{ title }}</div>
        <a class="ghost-btn link-btn" :href="imageUrl">下载</a>
      </div>
      <div class="image-stage">
        <img class="image-stage-img" :src="imageUrl" :alt="title" />
      </div>
    </div>
  `,
    computed: {
        title() {
            return this.$route.query.name || "图片查看";
        },
        imageUrl() {
            return `/api/file/download/${encodePath(this.$route.query.path || "")}`;
        }
    }
};
