import {confirm, get, normalizePageResult, ok, post, warn} from "../../common/http.js";

export default {
    name: "BookListPage",
    template: `
    <div class="card">
      <div class="toolbar">
        <el-input v-model="query.name" placeholder="书籍名称" style="width: 260px;" @keyup.enter="search" />
        <el-button type="primary" @click="search">搜索</el-button>
        <el-button @click="reset">重置</el-button>
        <el-button type="success" @click="$router.push('/book/form/new')">新增书籍</el-button>
        <el-button type="danger" @click="batchDelete">批量删除</el-button>
        <el-tag v-if="query.siteid" type="info">按站点过滤: {{ query.siteid }}</el-tag>
      </div>
      <el-table v-loading="loading" :data="list" @selection-change="selection = $event" @row-dblclick="onRowDblClick" border>
        <el-table-column type="selection" width="50" />
        <el-table-column label="ID" width="90">
          <template #default="scope">
            <span class="plain-action" @click="showChapter(scope.row)">{{ scope.row.bookid }}</span>
          </template>
        </el-table-column>
        <el-table-column label="名称" min-width="180">
          <template #default="scope">
            <span class="plain-action" @click="edit(scope.row)">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="站点" min-width="160">
          <template #default="scope">
            <span class="plain-action" @click="showSite(scope.row)">{{ scope.row.sitename }}</span>
          </template>
        </el-table-column>
        <el-table-column label="配置" min-width="160">
          <template #default="scope">
            <span class="plain-action" @click="showConfig(scope.row)">{{ scope.row.configname }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="taskstatusname" label="任务状态" width="120" />
        <el-table-column prop="taskswitchname" label="任务开关" width="120" />
        <el-table-column prop="lastgettime" label="最后获取时间" min-width="170" />
        <el-table-column prop="url" label="链接" min-width="250" show-overflow-tooltip />
        <el-table-column prop="starturl" label="起始章节链接" min-width="250" show-overflow-tooltip />
        <el-table-column label="操作" width="320" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="showChapter(scope.row)">章节</el-button>
            <el-button size="small" type="primary" @click="edit(scope.row)">编辑</el-button>
            <el-button size="small" type="warning" @click="download(scope.row, 1)">下载</el-button>
            <el-button size="small" type="warning" @click="download(scope.row, 2)">下载2</el-button>
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
            query: { name: "", siteid: null, pageNum: 1, pageSize: 10 }
        };
    },
    mounted() {
        this.syncSiteid();
        this.load();
    },
    watch: {
        "$route.params.siteid"() {
            this.syncSiteid();
            this.search();
        }
    },
    methods: {
        syncSiteid() {
            const raw = this.$route.params.siteid;
            this.query.siteid = raw ? Number(raw) : null;
        },
        async load() {
            this.loading = true;
            try {
                const data = await post("/api/queryBookList", this.query);
                const page = normalizePageResult(data);
                this.list = page.list;
                this.total = page.total;
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
            this.$router.push(`/book/form/${row.bookid}`);
        },
        showChapter(row) {
            this.$router.push(`/chapter/list/${row.bookid}`);
        },
        showSite(row) {
            this.$router.push(`/site/form/${row.siteid}`);
        },
        showConfig(row) {
            this.$router.push(`/config/form/${row.configid}`);
        },
        download(row, type) {
            const url = type === 1 ? `/api/downloadBook/${row.bookid}` : `/api/downloadBook2/${row.bookid}`;
            window.location.href = url;
        },
        async remove(row) {
            if (!(await confirm(`确认删除书籍【${row.name}】？`))) {
                return;
            }
            await get(`/api/deleteBook/${row.bookid}`);
            ok("删除成功");
            this.load();
        },
        async batchDelete() {
            if (!this.selection.length) {
                warn("请先选择要删除的数据");
                return;
            }
            if (!(await confirm(`确认删除已选 ${this.selection.length} 条书籍数据？`))) {
                return;
            }
            const ids = this.selection.map((item) => item.bookid).join(",");
            await get(`/api/deleteBook/${ids}`);
            ok("删除成功");
            this.load();
        }
    }
};
