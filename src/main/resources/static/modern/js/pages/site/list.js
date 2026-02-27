import {confirm, get, normalizePageResult, ok, post, warn} from "../../common/http.js";

export default {
    name: "SiteListPage",
    template: `
    <div class="card">
      <div class="toolbar">
        <el-input v-model="query.name" placeholder="站点名称" style="width: 260px;" @keyup.enter="search" />
        <el-button type="primary" @click="search">搜索</el-button>
        <el-button @click="reset">重置</el-button>
        <el-button type="success" @click="$router.push('/site/form/new')">新增站点</el-button>
        <el-button type="danger" @click="batchDelete">批量删除</el-button>
      </div>
      <el-table v-loading="loading" :data="list" @selection-change="selection = $event" @row-dblclick="onRowDblClick" border>
        <el-table-column type="selection" width="50" />
        <el-table-column label="ID" width="90">
          <template #default="scope">
            <span class="plain-action" @click="showBooks(scope.row)">{{ scope.row.siteid }}</span>
          </template>
        </el-table-column>
        <el-table-column label="名称" min-width="180">
          <template #default="scope">
            <span class="plain-action" @click="edit(scope.row)">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="配置" min-width="180">
          <template #default="scope">
            <span class="plain-action" @click="editConfig(scope.row)">{{ scope.row.configname }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="url" label="链接" min-width="280" show-overflow-tooltip />
        <el-table-column prop="createtime" label="创建时间" min-width="170" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="showBooks(scope.row)">书籍</el-button>
            <el-button size="small" type="primary" @click="edit(scope.row)">编辑</el-button>
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
            query: {
                name: "",
                pageNum: 1,
                pageSize: 10
            }
        };
    },
    mounted() {
        this.load();
    },
    methods: {
        async load() {
            this.loading = true;
            try {
                const data = await post("/api/querySiteList", this.query);
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
            this.$router.push(`/site/form/${row.siteid}`);
        },
        showBooks(row) {
            this.$router.push(`/book/list/${row.siteid}`);
        },
        editConfig(row) {
            if (!row.configid) {
                warn("该站点未绑定配置");
                return;
            }
            this.$router.push(`/config/form/${row.configid}`);
        },
        async remove(row) {
            if (!(await confirm(`确认删除站点【${row.name}】？`))) {
                return;
            }
            await get(`/api/deleteSite/${row.siteid}`);
            ok("删除成功");
            this.load();
        },
        async batchDelete() {
            if (!this.selection.length) {
                warn("请先选择要删除的数据");
                return;
            }
            if (!(await confirm(`确认删除已选 ${this.selection.length} 条站点数据？`))) {
                return;
            }
            const ids = this.selection.map((item) => item.siteid).join(",");
            await get(`/api/deleteSite/${ids}`);
            ok("删除成功");
            this.load();
        }
    }
};
