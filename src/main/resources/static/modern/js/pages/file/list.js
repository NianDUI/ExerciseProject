import {encodePath, get, warn} from "../../common/http.js";

function isVideo(name) {
    if (!name) {
        return false;
    }
    const lower = name.toLowerCase();
    return lower.endsWith(".mp4") || lower.endsWith(".mkv") || lower.endsWith(".m3u8") || lower.endsWith(".webm");
}

export default {
    name: "FileListPage",
    template: `
    <div class="card">
      <div class="toolbar">
        <el-button @click="$router.push('/dashboard')">首页</el-button>
        <el-button @click="goParent" :disabled="!parentPath">上级目录</el-button>
        <el-tag type="info">当前路径：{{ currentPathName }}</el-tag>
      </div>
      <el-table :data="list" v-loading="loading" border>
        <el-table-column label="名称" min-width="320">
          <template #default="scope">
            <span class="plain-action" @click="openItem(scope.row)">
              {{ scope.row.name }}<span v-if="scope.row.isDir">/</span>
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="size" label="大小" width="120">
          <template #default="scope">{{ scope.row.isDir ? '-' : (scope.row.size + (scope.row.unit || '')) }}</template>
        </el-table-column>
        <el-table-column prop="lastModified" label="修改时间" min-width="180" />
      </el-table>

      <el-dialog v-model="videoVisible" title="视频预览" width="80%">
        <video v-if="videoVisible" :src="videoUrl" style="width: 100%;" controls />
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
            videoUrl: ""
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
                const url = encoded ? `/api/file/list/${encoded}` : "/api/file/list/";
                const pathUrl = encoded ? `/api/file/path/${encoded}` : "/api/file/path";
                const [list, pathName] = await Promise.all([get(url), get(pathUrl)]);
                this.list = list || [];
                this.currentPathName = pathName || "/";
                const parent = (this.list || []).find((item) => item && item.name === ".." && item.isDir);
                this.parentPath = parent && parent.path ? parent.path : "";
            } finally {
                this.loading = false;
            }
        },
        goParent() {
            if (!this.parentPath) {
                return;
            }
            this.$router.push(`/file/list/${this.parentPath}`);
        },
        openItem(row) {
            if (row.isDir) {
                this.$router.push(`/file/list/${row.path}`);
                return;
            }
            if (isVideo(row.name)) {
                this.preview(row);
                return;
            }
            this.download(row);
        },
        download(row) {
            if (!row.path) {
                warn("路径无效");
                return;
            }
            window.location.href = `/api/file/download/${encodePath(row.path)}`;
        },
        preview(row) {
            if (row.isDir) {
                return;
            }
            if (!isVideo(row.name)) {
                warn("当前仅内置视频预览，其他类型请下载查看");
                return;
            }
            this.videoUrl = `/api/file/download/${encodePath(row.path)}`;
            this.videoVisible = true;
        }
    }
};
