import {get, ok, post} from "../../common/http.js";

export default {
    name: "ChapterFormPage",
    template: `
    <div class="card">
      <el-page-header @back="$router.back()" content="章节编辑" />
      <el-divider />
      <el-form :model="form" label-width="120px" style="max-width: 820px;">
        <el-form-item label="名称"><el-input v-model.trim="form.name" /></el-form-item>
        <el-form-item label="原名称"><el-input v-model.trim="form.rawname" /></el-form-item>
        <el-form-item label="书籍">
          <el-select v-model="form.bookid" disabled style="width: 100%;">
            <el-option v-for="item in optionBook" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="配置">
          <el-select v-model="form.configid" disabled style="width: 100%;">
            <el-option v-for="item in optionConfig" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序ID"><el-input-number v-model="form.seqid" :min="1" /></el-form-item>
        <el-form-item label="链接"><el-input v-model.trim="form.url" /></el-form-item>
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
            optionBook: [],
            optionConfig: [],
            form: { chapterid: null, name: "", rawname: "", bookid: null, configid: null, seqid: 1, url: "" },
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
        const [books, configs] = await Promise.all([get("/api/optionBook"), get("/api/optionConfig")]);
        this.optionBook = books || [];
        this.optionConfig = configs || [];
        if (this.id) {
            await this.loadModel();
        }
    },
    methods: {
        async loadModel() {
            const data = await get(`/api/modelChapter/${this.id}`);
            this.form = { ...data };
            this.origin = { ...data };
        },
        reset() {
            if (this.origin) {
                this.form = { ...this.origin };
            }
        },
        async submit() {
            if (!this.form.name || !this.form.rawname || !this.form.seqid || !this.form.url) {
                window.ElementPlus.ElMessage.warning("请完善必填字段");
                return;
            }
            this.form.chapterid = this.id || null;
            this.saving = true;
            try {
                await post("/api/saveChapter", this.form);
                ok("提交成功");
                this.$router.back();
            } finally {
                this.saving = false;
            }
        }
    }
};
