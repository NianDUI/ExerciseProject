import router from "./router/index.js";
import {setTokenByPrompt} from "./common/auth.js";

const App = {
    template: `
    <el-container class="layout">
      <el-aside width="220px">
        <el-menu :default-active="activeMenu" @select="selectMenu" style="height: 100%;">
          <el-menu-item index="/dashboard">仪表盘</el-menu-item>
          <el-menu-item index="/book/list">书籍管理</el-menu-item>
          <el-menu-item index="/site/list">站点管理</el-menu-item>
          <el-menu-item index="/config/list">配置管理</el-menu-item>
          <el-menu-item index="/file/list">文件管理</el-menu-item>
          <el-menu-item index="/log/live">日志中心</el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header style="display: flex; align-items: center; justify-content: space-between;">
          <div>
            <strong style="font-size: 16px;">fictionweb 新管理后台</strong>
          </div>
          <div class="row-gap">
            <el-button size="small" @click="refreshToken">设置 Token</el-button>
            <el-button size="small" @click="goLegacy">旧版首页</el-button>
          </div>
        </el-header>
        <el-main class="main-scroll">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  `,
    computed: {
        activeMenu() {
            const p = this.$route.path;
            if (p.startsWith("/site")) return "/site/list";
            if (p.startsWith("/book")) return "/book/list";
            if (p.startsWith("/config")) return "/config/list";
            if (p.startsWith("/chapter")) return "/book/list";
            if (p.startsWith("/file")) return "/file/list";
            if (p.startsWith("/log")) return "/log/live";
            return "/dashboard";
        }
    },
    methods: {
        selectMenu(path) {
            if (this.$route.path !== path) {
                this.$router.push(path);
            }
        },
        refreshToken() {
            setTokenByPrompt();
            window.ElementPlus.ElMessage.success("Token 更新成功");
        },
        goLegacy() {
            window.location.href = "/main";
        }
    }
};

const app = window.Vue.createApp(App);
app.use(window.ElementPlus);
app.use(router);
app.mount("#app");
