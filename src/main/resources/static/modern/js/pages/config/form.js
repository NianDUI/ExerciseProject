import {get, ok, post} from "../../common/http.js";

export default {
    name: "ConfigFormPage",
    template: `
    <div class="card">
      <el-page-header @back="$router.back()" content="配置编辑" />
      <el-divider />
      <el-form :model="form" label-width="140px" style="max-width: 920px;">
        <el-form-item label="名称"><el-input v-model.trim="form.name" /></el-form-item>
        <el-form-item label="标题匹配路径"><el-input v-model.trim="form.titlematch" /></el-form-item>
        <el-form-item label="标题后换行数量"><el-input-number v-model="form.titlelnnum" :min="0" /></el-form-item>
        <el-form-item label="内容匹配路径"><el-input v-model.trim="form.conmatch" /></el-form-item>
        <el-form-item label="内容后换行数量"><el-input-number v-model="form.conlnnum" :min="0" /></el-form-item>
        <el-form-item label="内容开始偏移"><el-input-number v-model="form.startoffset" :min="0" /></el-form-item>
        <el-form-item label="内容结束偏移"><el-input-number v-model="form.endoffset" :max="0" /></el-form-item>
        <el-form-item label="跳转超链接匹配"><el-input v-model.trim="form.amatch" /></el-form-item>
        <el-form-item label="下一页索引"><el-input-number v-model="form.nexta" :min="0" /></el-form-item>
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
            form: {
                configid: null,
                name: "",
                titlematch: "",
                titlelnnum: 2,
                conmatch: "",
                conlnnum: 2,
                startoffset: 0,
                endoffset: 0,
                amatch: "",
                nexta: 0
            },
            origin: null
        };
    },
    computed: {
        id() {
            const id = this.$route.params.id;
            return !id || id === "new" ? null : Number(id);
        },
        copyFrom() {
            const val = this.$route.query.copyFrom;
            return val ? Number(val) : null;
        }
    },
    async mounted() {
        if (this.id || this.copyFrom) {
            const targetId = this.id || this.copyFrom;
            const data = await get(`/api/modelConfig/${targetId}`);
            this.form = { ...data };
            if (this.copyFrom && !this.id) {
                this.form.configid = null;
            }
            this.origin = { ...this.form };
        }
    },
    methods: {
        reset() {
            if (this.origin) {
                this.form = { ...this.origin };
            }
        },
        async submit() {
            if (!this.form.name || !this.form.titlematch || !this.form.conmatch || !this.form.amatch) {
                window.ElementPlus.ElMessage.warning("请完善必填字段");
                return;
            }
            this.form.configid = this.id || null;
            this.saving = true;
            try {
                await post("/api/saveConfig", this.form);
                ok("提交成功");
                this.$router.back();
            } finally {
                this.saving = false;
            }
        }
    }
};
