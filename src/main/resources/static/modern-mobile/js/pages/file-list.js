import {encodePath, get, warn} from "../../../modern/js/common/http.js";

function isVideo(name) {
    const lower = String(name || "").toLowerCase();
    return lower.endsWith(".mp4") || lower.endsWith(".mkv") || lower.endsWith(".webm") || lower.endsWith(".m3u8");
}

function isText(name) {
    const lower = String(name || "").toLowerCase();
    return lower.endsWith(".txt") || lower.endsWith(".log") || lower.endsWith(".md") || lower.endsWith(".json");
}

export default {
    name: "MobileFileListPage",
    template: `
    <div class="mobile-page">
      <section class="mobile-section">
        <div class="catalog-head">
          <div>
            <p class="hero-kicker">文件</p>
            <h2 class="catalog-title">轻量文件管理</h2>
          </div>
          <el-button round :disabled="!parentPath" @click="goParent">上级目录</el-button>
        </div>

        <div class="path-chip">{{ currentPathName }}</div>

        <div class="file-list" v-loading="loading">
          <button v-for="item in list" :key="item.path + item.name" class="file-item" @click="openItem(item)">
            <span class="file-icon">{{ iconOf(item) }}</span>
            <span class="file-name">{{ item.name }}<span v-if="item.isDir">/</span></span>
            <small class="file-meta">{{ item.isDir ? '目录' : ((item.size || '') + (item.unit || '')) }} · {{ item.lastModified || '-' }}</small>
          </button>
        </div>
      </section>

      <el-dialog v-model="videoVisible" fullscreen title="视频预览">
        <video v-if="videoVisible" :src="videoUrl" style="width: 100%;" controls playsinline />
      </el-dialog>

      <el-dialog v-model="textVisible" fullscreen title="文本预览">
        <pre class="text-preview">{{ textContent }}</pre>
      </el-dialog>
    </div>
  `,
    data() {
        return {
            loading: false,
            list: [],
            currentPathName: "/",
            parentPath: "",
            videoVisible: false,
            videoUrl: "",
            textVisible: false,
            textContent: ""
        };
    },
    computed: {
        pathRaw() {
            const p = this.$route.params.pathMatch;
            if (!p) {
                return "";
            }
            return Array.isArray(p) ? p.join("/") : p;
        }
    },
    mounted() {
        this.load();
    },
    watch: {
        "$route.fullPath"() {
            this.load();
        }
    },
    methods: {
        async load() {
            this.loading = true;
            try {
                const encoded = encodePath(this.pathRaw);
                const listUrl = encoded ? `/api/file/list/${encoded}` : "/api/file/list/";
                const pathUrl = encoded ? `/api/file/path/${encoded}` : "/api/file/path";
                const [list, pathName] = await Promise.all([get(listUrl), get(pathUrl)]);
                this.list = list || [];
                this.currentPathName = pathName || "/";
                const parent = this.list.find((item) => item && item.name === ".." && item.isDir);
                this.parentPath = parent && parent.path ? parent.path : "";
            } finally {
                this.loading = false;
            }
        },
        goParent() {
            if (!this.parentPath) {
                return;
            }
            this.$router.push(`/files/${this.parentPath}`);
        },
        async openItem(item) {
            if (item.isDir) {
                this.$router.push(`/files/${item.path}`);
                return;
            }
            if (isVideo(item.name)) {
                this.videoUrl = `/api/file/download/${encodePath(item.path)}`;
                this.videoVisible = true;
                return;
            }
            if (isText(item.name)) {
                await this.previewText(item);
                return;
            }
            if (!item.path) {
                warn("文件路径无效");
                return;
            }
            window.location.href = `/api/file/download/${encodePath(item.path)}`;
        },
        async previewText(item) {
            if (!item.path) {
                warn("文件路径无效");
                return;
            }
            const token = window.Cookies.get("token") || window.sessionStorage.getItem("token") || "";
            const res = await window.axios.get(`/api/file/read?path=${encodeURIComponent(item.path)}`, {
                headers: { token },
                responseType: "text"
            });
            this.textContent = typeof res.data === "string" ? res.data : "";
            this.textVisible = true;
        },
        iconOf(item) {
            if (item.isDir) {
                return "DIR";
            }
            if (isVideo(item.name)) {
                return "VID";
            }
            if (isText(item.name)) {
                return "TXT";
            }
            return "FILE";
        }
    }
};
