import {encodePath} from "../../../modern/js/common/http.js";

export default {
    name: "MobileTextViewerPage",
    template: `
    <div class="text-viewer-page">
      <div class="video-player-topbar">
        <button class="ghost-btn" @click="$router.back()">返回</button>
        <div class="video-player-title">{{ title }}</div>
        <a class="ghost-btn link-btn" :href="downloadUrl">下载</a>
      </div>
      <div class="text-viewer-body">
        <pre class="text-preview mobile-text-view" v-loading="loading">{{ textContent }}</pre>
      </div>
    </div>
  `,
    data() {
        return {
            loading: false,
            textContent: ""
        };
    },
    computed: {
        rawPath() {
            return this.$route.query.path || "";
        },
        title() {
            return this.$route.query.name || "文本查看";
        },
        downloadUrl() {
            return `/api/file/download/${encodePath(this.rawPath)}`;
        }
    },
    mounted() {
        this.load();
    },
    methods: {
        async load() {
            this.loading = true;
            try {
                const token = window.Cookies.get("token") || window.sessionStorage.getItem("token") || "";
                const res = await window.axios.get(`/api/file/read?path=${encodeURIComponent(this.rawPath)}`, {
                    headers: { token },
                    responseType: "text"
                });
                this.textContent = typeof res.data === "string" ? res.data : "";
            } finally {
                this.loading = false;
            }
        }
    }
};
