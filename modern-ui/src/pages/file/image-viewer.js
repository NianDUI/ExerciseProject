import {encodePath} from "../../common/http.js";

export default {
    name: "FileImageViewerPage",
    template: `
    <div class="card image-viewer-card">
      <div class="toolbar">
        <el-button @click="$router.back()">返回</el-button>
        <el-tag type="info" effect="dark">{{ title }}</el-tag>
        <el-button type="primary" @click="download">下载图片</el-button>
      </div>
      <div class="image-stage pc">
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
    },
    methods: {
        download() {
            window.location.href = this.imageUrl;
        }
    }
};
