import {confirm, get, normalizePageResult, ok, post, warn} from "../../common/http.js";

export default {
    name: "ConfigListPage",
    template: `
    <div class="card">
      <div class="toolbar">
        <el-input v-model="query.name" placeholder="配置名称" style="width: 260px;" @keyup.enter="search" />
        <el-button type="primary" @click="search">搜索</el-button>
        <el-button @click="reset">重置</el-button>
        <el-button type="success" @click="$router.push('/config/form/new')">新增配置</el-button>
        <el-button type="danger" @click="batchDelete">批量删除</el-button>
      </div>
      <el-table v-loading="loading" :data="list" @selection-change="selection = $event" @row-dblclick="onRowDblClick" border>
        <el-table-column type="selection" width="50" />
        <el-table-column prop="configid" label="ID" width="90" />
        <el-table-column label="名称" min-width="140">
          <template #default="scope">
            <span class="plain-action" @click="edit(scope.row)">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="titlematch" label="标题匹配" min-width="220" show-overflow-tooltip />
        <el-table-column prop="conmatch" label="内容匹配" min-width="220" show-overflow-tooltip />
        <el-table-column prop="amatch" label="跳转匹配" min-width="220" show-overflow-tooltip />
        <el-table-column prop="nexta" label="下一页索引" width="110" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" @click="edit(scope.row)">编辑</el-button>
            <el-button size="small" type="warning" @click="copy(scope.row)">复制</el-button>
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
            query: { name: "", pageNum: 1, pageSize: 10 }
        };
    },
    mounted() {
        this.load();
    },
    methods: {
        async load() {
            this.loading = true;
            try {
                const data = await post("/api/queryConfigList", this.query);
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
            this.$router.push(`/config/form/${row.configid}`);
        },
        copy(row) {
            this.$router.push(`/config/form/new?copyFrom=${row.configid}`);
        },
        async remove(row) {
            if (!(await confirm(`确认删除配置【${row.name}】？`))) {
                return;
            }
            await get(`/api/deleteConfig/${row.configid}`);
            ok("删除成功");
            this.load();
        },
        async batchDelete() {
            if (!this.selection.length) {
                warn("请先选择要删除的数据");
                return;
            }
            if (!(await confirm(`确认删除已选 ${this.selection.length} 条配置数据？`))) {
                return;
            }
            const ids = this.selection.map((item) => item.configid).join(",");
            await get(`/api/deleteConfig/${ids}`);
            ok("删除成功");
            this.load();
        }
    }
};
