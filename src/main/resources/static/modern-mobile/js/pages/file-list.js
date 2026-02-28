import {encodePath, get, warn} from "../../../modern/js/common/http.js";
import {isImageFile, isTextFile, isVideoFile} from "../../../modern/js/common/file-viewer.js";
import {getVideoMimeType} from "../../../modern/js/common/video-player.js";

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

    </div>
  `,
    data() {
        return {
            loading: false,
            list: [],
            currentPathName: "/",
            parentPath: ""
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
        openItem(item) {
            if (item.isDir) {
                this.$router.push(`/files/${item.path}`);
                return;
            }
            if (isVideoFile(item.name)) {
                this.$router.push({
                    path: "/player/video",
                    query: {
                        path: item.path,
                        name: item.name || "",
                        type: getVideoMimeType(item.name)
                    }
                });
                return;
            }
            if (isImageFile(item.name)) {
                this.$router.push({
                    path: "/viewer/image",
                    query: {
                        path: item.path,
                        name: item.name || ""
                    }
                });
                return;
            }
            if (isTextFile(item.name)) {
                this.$router.push({
                    path: "/viewer/text",
                    query: {
                        path: item.path,
                        name: item.name || ""
                    }
                });
                return;
            }
            if (!item.path) {
                warn("文件路径无效");
                return;
            }
            window.location.href = `/api/file/download/${encodePath(item.path)}`;
        },
        iconOf(item) {
            if (item.isDir) {
                return "DIR";
            }
            if (isVideoFile(item.name)) {
                return "VID";
            }
            if (isImageFile(item.name)) {
                return "IMG";
            }
            if (isTextFile(item.name)) {
                return "TXT";
            }
            return "FILE";
        }
    }
};
