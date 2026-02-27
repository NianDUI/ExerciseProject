import {get, ok, post} from "../../common/http.js";

export default {
    name: "SiteFormPage",
    template: `
    <div class="card">
      <el-page-header @back="$router.back()" content="站点编辑" />
      <el-divider />
      <el-form :model="form" label-width="110px" style="max-width: 760px;">
        <el-form-item label="名称">
          <el-input v-model.trim="form.name" placeholder="请输入站点名称" />
        </el-form-item>
        <el-form-item label="配置">
          <el-select v-model="form.configid" placeholder="请选择配置" style="width: 100%;">
            <el-option v-for="item in optionConfig" :key="item.id" :value="item.id" :label="item.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="链接">
          <el-input v-model.trim="form.url" type="textarea" :rows="3" placeholder="站点链接" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="saving" @click="submit">提交</el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  `,
    data() {
        return {
            saving: false,
            optionConfig: [],
            form: { siteid: null, name: "", configid: null, url: "" },
            origin: null
        };
    },
    computed: {
        id() {
            const id = this.$route.params.id;
            return !id || id === "new" ? null : Number(id);
        }
    },
    async mounted() {
        this.optionConfig = await get("/api/optionConfig");
        if (this.id) {
            await this.loadModel();
        }
    },
    methods: {
        async loadModel() {
            const data = await get(`/api/modelSite/${this.id}`);
            this.form = { ...data };
            this.origin = { ...data };
        },
        reset() {
            if (this.origin) {
                this.form = { ...this.origin };
                return;
            }
            this.form = { siteid: null, name: "", configid: null, url: "" };
        },
        async submit() {
            if (!this.form.name) {
                window.ElementPlus.ElMessage.warning("名称不能为空");
                return;
            }
            if (!this.form.configid) {
                window.ElementPlus.ElMessage.warning("配置不能为空");
                return;
            }
            this.form.siteid = this.id;
            this.saving = true;
            try {
                await post("/api/saveSite", this.form);
                ok("提交成功");
                this.$router.back();
            } finally {
                this.saving = false;
            }
        }
    }
};
