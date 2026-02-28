import router from "./router/index.js";

const TABS = [
    { path: "/", label: "书城" },
    { path: "/files", label: "文件" },
    { path: "/me", label: "我的" }
];

const App = {
    template: `
    <div class="mobile-shell">
      <div class="mobile-shell-bg"></div>
      <header v-if="!isReader" class="mobile-header">
        <div>
          <p class="mobile-brand">fictionweb</p>
          <h1>{{ pageTitle }}</h1>
        </div>
        <button class="header-action" @click="goLegacy">后台</button>
      </header>

      <main class="mobile-main" :class="{ reader: isReader }">
        <router-view />
      </main>

      <nav v-if="!isReader" class="mobile-tabbar">
        <button
          v-for="item in tabs"
          :key="item.path"
          class="tab-item"
          :class="{ active: activeTab === item.path }"
          @click="go(item.path)">
          {{ item.label }}
        </button>
      </nav>
    </div>
  `,
    data() {
        return {
            tabs: TABS
        };
    },
    computed: {
        isReader() {
            return this.$route.path.startsWith("/reader/")
                || this.$route.path.startsWith("/player/video")
                || this.$route.path.startsWith("/viewer/image")
                || this.$route.path.startsWith("/viewer/text");
        },
        pageTitle() {
            return (this.$route.meta && this.$route.meta.title) || "手机端";
        },
        activeTab() {
            if (this.$route.path.startsWith("/files")) {
                return "/files";
            }
            if (this.$route.path.startsWith("/me")) {
                return "/me";
            }
            return "/";
        }
    },
    methods: {
        go(path) {
            if (this.$route.path !== path) {
                this.$router.push(path);
            }
        },
        goLegacy() {
            window.location.href = "/modern/main";
        }
    }
};

const app = window.Vue.createApp(App);
app.use(window.ElementPlus);
app.use(router);
app.mount("#app");
