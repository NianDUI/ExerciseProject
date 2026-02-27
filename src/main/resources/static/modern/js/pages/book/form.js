import {get, ok, post} from "../../common/http.js";

function parseHandlerInfo(raw) {
    try {
        const obj = raw ? JSON.parse(raw) : {};
        return {
            getUtil: obj.getUtil || "htmlunit",
            titleType: obj.titleType != null ? Number(obj.titleType) : 0,
            startIndex: obj.startIndex != null ? Number(obj.startIndex) : 0,
            delimiter: obj.delimiter || "",
            endType: obj.endType != null ? Number(obj.endType) : 0,
            endCharacter: obj.endCharacter || "",
            proxyid: obj.proxyid != null ? Number(obj.proxyid) : 0
        };
    } catch (e) {
        return { getUtil: "htmlunit", titleType: 0, startIndex: 0, delimiter: "", endType: 0, endCharacter: "", proxyid: 0 };
    }
}

export default {
    name: "BookFormPage",
    template: `
    <div class="card">
      <el-page-header @back="$router.back()" content="书籍编辑" />
      <el-divider />
      <el-form :model="form" label-width="130px" style="max-width: 920px;">
        <el-form-item label="名称"><el-input v-model.trim="form.name" /></el-form-item>
        <el-form-item label="站点">
          <el-select v-model="form.siteid" placeholder="请选择站点" style="width: 100%;" @change="changeSite">
            <el-option v-for="item in optionSite" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="配置">
          <el-select v-model="form.configid" placeholder="请选择配置" style="width: 100%;">
            <el-option v-for="item in optionConfig" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="书籍链接"><el-input v-model.trim="form.url" /></el-form-item>
        <el-form-item label="起始章节链接"><el-input v-model.trim="form.starturl" /></el-form-item>
        <el-form-item label="任务开关">
          <el-radio-group v-model="form.taskswitch">
            <el-radio :value="0">关闭</el-radio>
            <el-radio :value="1">开启</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="获取工具">
          <el-select v-model="handler.getUtil">
            <el-option label="htmlunit" value="htmlunit" />
            <el-option label="selenium" value="selenium" />
          </el-select>
        </el-form-item>
        <el-form-item label="标题处理方式">
          <el-select v-model="handler.titleType">
            <el-option label="自动匹配" :value="0" />
            <el-option label="使用分隔符" :value="1" />
            <el-option label="数字开头无分隔符" :value="2" />
            <el-option label="提取第x章..." :value="3" />
            <el-option label="提取数字" :value="4" />
            <el-option label="使用序号" :value="5" />
            <el-option label="无处理" :value="-1" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理起始索引"><el-input-number v-model="handler.startIndex" :min="0" /></el-form-item>
        <el-form-item v-if="handler.titleType === 1" label="分隔符"><el-input v-model="handler.delimiter" /></el-form-item>
        <el-form-item label="结束链接判断方式">
          <el-select v-model="handler.endType">
            <el-option label="不以html结尾或和本页链接相同" :value="0" />
            <el-option label="以指定结束符结尾" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="handler.endType === 1" label="结束符"><el-input v-model="handler.endCharacter" /></el-form-item>
        <el-form-item label="代理">
          <el-select v-model="handler.proxyid">
            <el-option label="无" :value="0" />
            <el-option v-for="item in optionProxy" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="id" label="详情"><el-input v-model="form.detail" type="textarea" :rows="3" readonly /></el-form-item>
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
            optionSite: [],
            optionConfig: [],
            optionProxy: [],
            form: {
                bookid: null,
                name: "",
                siteid: null,
                configid: null,
                url: "",
                starturl: "",
                taskswitch: 1,
                detail: ""
            },
            handler: {
                getUtil: "htmlunit",
                titleType: 0,
                startIndex: 0,
                delimiter: "",
                endType: 0,
                endCharacter: "",
                proxyid: 0
            },
            originForm: null,
            originHandler: null
        };
    },
    computed: {
        id() {
            const id = this.$route.params.id;
            return !id || id === "new" ? null : Number(id);
        }
    },
    async mounted() {
        const [sites, configs, proxies] = await Promise.all([
            get("/api/optionSite"),
            get("/api/optionConfig"),
            get("/api/optionProxy")
        ]);
        this.optionSite = sites || [];
        this.optionConfig = configs || [];
        this.optionProxy = proxies || [];

        if (this.id) {
            const data = await get(`/api/modelBook/${this.id}`);
            this.form = { ...data };
            this.handler = parseHandlerInfo(data.handlerinfo);
            this.originForm = { ...this.form };
            this.originHandler = { ...this.handler };
        }
    },
    methods: {
        async changeSite(siteid) {
            if (!siteid) {
                return;
            }
            const site = await get(`/api/modelSite/${siteid}`);
            if (site && site.configid) {
                this.form.configid = site.configid;
            }
        },
        reset() {
            if (this.originForm) {
                this.form = { ...this.originForm };
                this.handler = { ...this.originHandler };
            }
        },
        async submit() {
            if (!this.form.name || !this.form.siteid || !this.form.configid || !this.form.starturl) {
                window.ElementPlus.ElMessage.warning("请完善必填字段");
                return;
            }
            const payload = {
                ...this.form,
                bookid: this.id || null,
                handlerinfo: JSON.stringify({
                    getUtil: this.handler.getUtil,
                    titleType: Number(this.handler.titleType),
                    startIndex: Number(this.handler.startIndex),
                    delimiter: this.handler.delimiter || "",
                    endType: Number(this.handler.endType),
                    endCharacter: this.handler.endCharacter || "",
                    proxyid: Number(this.handler.proxyid)
                })
            };
            this.saving = true;
            try {
                await post("/api/saveBook", payload);
                ok("提交成功");
                this.$router.back();
            } finally {
                this.saving = false;
            }
        }
    }
};
