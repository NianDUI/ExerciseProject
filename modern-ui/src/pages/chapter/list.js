import {confirm, get, normalizePageResult, ok, post, warn} from "../../common/http.js";

export default {
    name: "ChapterListPage",
    template: `
    <div class="card">
      <div class="toolbar">
        <el-input v-model="query.name" placeholder="章节名称" style="width: 260px;" @keyup.enter="search" />
        <el-button type="primary" @click="search">搜索</el-button>
        <el-button @click="reset">重置</el-button>
        <el-button type="danger" @click="batchDelete">批量删除</el-button>
        <template v-if="book.taskswitch !== 0">
          <el-button type="warning" @click="reacquireAll">重新获取</el-button>
          <el-button type="warning" @click="getFollowUp">获取后续</el-button>
          <el-button type="warning" @click="getSpecifiedAndFollowUp">获取指定和后续</el-button>
          <el-button v-if="polling" type="danger" @click="stopGet">停止获取</el-button>
        </template>
      </div>
      <el-alert v-if="book.bookid" :title="'当前书籍：' + book.name" type="info" :closable="false" style="margin-bottom: 10px;" />
      <el-table v-loading="loading" :data="list" @selection-change="selection = $event" @row-dblclick="onRowDblClick" border>
        <el-table-column type="selection" width="50" />
        <el-table-column label="ID" width="90">
          <template #default="scope">
            <span class="plain-action" @click="view(scope.row)">{{ scope.row.chapterid }}</span>
          </template>
        </el-table-column>
        <el-table-column label="名称" min-width="180">
          <template #default="scope">
            <span class="plain-action" @click="edit(scope.row)">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="原名称" min-width="180">
          <template #default="scope">
            <span class="plain-action" @click="edit(scope.row)">{{ scope.row.rawname }}</span>
          </template>
        </el-table-column>
        <el-table-column label="书籍" min-width="150">
          <template #default="scope">
            <span class="plain-action" @click="showBook(scope.row)">{{ scope.row.bookname }}</span>
          </template>
        </el-table-column>
        <el-table-column label="配置" min-width="150">
          <template #default="scope">
            <span class="plain-action" @click="showConfig(scope.row)">{{ scope.row.configname }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createtime" label="创建时间" min-width="170" />
        <el-table-column prop="url" label="链接" min-width="260" show-overflow-tooltip />
        <el-table-column label="操作" width="360" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="view(scope.row)">查看</el-button>
            <el-button size="small" type="primary" @click="edit(scope.row)">编辑</el-button>
            <el-button size="small" type="warning" @click="refreshOne(scope.row)">刷新</el-button>
            <el-button size="small" type="danger" @click="remove(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        style="margin-top: 14px;"
        background
        layout="total, prev, pager, next, sizes"
        v-model:current-page="query.pageNum"
        v-model:page-size="query.pageSize"
        :page-sizes="[10,20,50,100]"
        :total="total"
        @current-change="load"
        @size-change="sizeChange"
      />
    </div>
  `,
    data() {
        return {
            loading: false,
            list: [],
            selection: [],
            total: 0,
            query: { name: "", bookid: null, pageNum: 1, pageSize: 10 },
            book: { bookid: null, name: "", taskswitch: 0, taskstatus: 0 },
            polling: false,
            timer: null
        };
    },
    async mounted() {
        await this.syncBookid();
        await this.load();
    },
    beforeUnmount() {
        this.stopTimer();
    },
    watch: {
        "$route.params.bookid": {
            async handler() {
                await this.syncBookid();
                this.search();
            }
        }
    },
    methods: {
        async syncBookid() {
            const raw = this.$route.params.bookid;
            this.query.bookid = raw ? Number(raw) : null;
            if (this.query.bookid) {
                this.book = await get(`/api/modelBook/${this.query.bookid}`);
                if (this.book.taskswitch !== 0 && this.book.taskstatus !== 0) {
                    this.startTimer();
                }
            } else {
                this.book = { bookid: null, name: "", taskswitch: 0, taskstatus: 0 };
                this.stopTimer();
            }
        },
        async load() {
            if (!this.query.bookid) {
                this.list = [];
                this.total = 0;
                return;
            }
            this.loading = true;
            try {
                const data = await post("/api/queryChapterList", this.query);
                const page = normalizePageResult(data);
                this.list = page.list;
                this.total = page.total;
                this.query.pageNum = page.pageNum;
                this.query.pageSize = page.pageSize;
                await this.checkStatus();
            } finally {
                this.loading = false;
            }
        },
        search() {
            this.query.pageNum = 1;
            this.load();
        },
        reset() {
            this.query.name = "";
            this.search();
        },
        sizeChange() {
            this.query.pageNum = 1;
            this.load();
        },
        onRowDblClick(row) {
            this.edit(row);
        },
        edit(row) {
            this.$router.push(`/chapter/form/${row.chapterid}`);
        },
        view(row) {
            this.$router.push(`/chapter/view/${row.chapterid}`);
        },
        showBook(row) {
            this.$router.push(`/book/form/${row.bookid}`);
        },
        showConfig(row) {
            this.$router.push(`/config/form/${row.configid}`);
        },
        async remove(row) {
            if (!(await confirm(`确认删除章节【${row.name}】？`))) {
                return;
            }
            await get(`/api/deleteChapter/${row.chapterid}`);
            ok("删除成功");
            this.load();
        },
        async batchDelete() {
            if (!this.selection.length) {
                warn("请先选择要删除的数据");
                return;
            }
            if (!(await confirm(`确认删除已选 ${this.selection.length} 条章节数据？`))) {
                return;
            }
            const ids = this.selection.map((item) => item.chapterid).join(",");
            await get(`/api/deleteChapter/${ids}`);
            ok("删除成功");
            this.load();
        },
        async refreshOne(row) {
            await get(`/api/reacquireSingleChapter/${row.chapterid}`);
            ok("刷新任务已触发");
            this.startTimer();
        },
        startTimer() {
            if (this.timer) {
                return;
            }
            this.polling = true;
            this.timer = window.setInterval(() => {
                this.query.pageNum = 9999999;
                this.load();
            }, 5000);
        },
        stopTimer() {
            if (this.timer) {
                window.clearInterval(this.timer);
                this.timer = null;
            }
            this.polling = false;
        },
        async checkStatus() {
            if (!this.query.bookid || this.book.taskswitch === 0) {
                return;
            }
            const status = await get(`/api/queryGetStatus/${this.query.bookid}`);
            if (status && status !== 0) {
                this.startTimer();
            } else {
                this.stopTimer();
            }
        },
        async reacquireAll() {
            await get(`/api/reacquireAllChapter/${this.query.bookid}`);
            ok("已触发重新获取");
            this.startTimer();
        },
        async getFollowUp() {
            await get(`/api/getFollowUpChapter/${this.query.bookid}`);
            ok("已触发获取后续");
            this.startTimer();
        },
        async getSpecifiedAndFollowUp() {
            const url = window.prompt("请输入章节链接：");
            if (!url || !url.trim()) {
                warn("请输入章节链接");
                return;
            }
            await post("/api/getSpecifiedAndFollowUpChapter", { bookid: this.query.bookid, url: url.trim() });
            ok("已触发指定和后续获取");
            this.startTimer();
        },
        async stopGet() {
            await get(`/api/stopGet/${this.query.bookid}`);
            ok("已停止获取");
            this.stopTimer();
        }
    }
};
