import { setTokenByPrompt } from './common/auth.js'

const THEME_OPTIONS = [
  { value: 'default', label: '经典深色' },
  { value: 'light', label: '浅色简约' },
  { value: 'glass', label: '毛玻璃' }
]

function applyTheme (name) {
  if (name && name !== 'default') {
    document.documentElement.setAttribute('data-theme', name)
  } else {
    document.documentElement.removeAttribute('data-theme')
  }
  localStorage.setItem('fw-theme', name || 'default')
}

export default {
  template: `
    <el-container class="layout">
      <el-aside width="220px">
        <div class="sidebar">
          <div class="sidebar-logo">
            <div class="logo-icon">F</div>
            <span class="logo-text">FictionWeb</span>
          </div>
          <el-menu :default-active="activeMenu" @select="selectMenu">
            <el-menu-item index="/dashboard">
              <el-icon class="menu-icon"><Odometer /></el-icon>
              <span class="menu-label">仪表盘</span>
            </el-menu-item>
            <el-menu-item index="/book/list">
              <el-icon class="menu-icon"><Reading /></el-icon>
              <span class="menu-label">书籍管理</span>
            </el-menu-item>
            <el-menu-item index="/site/list">
              <el-icon class="menu-icon"><Monitor /></el-icon>
              <span class="menu-label">站点管理</span>
            </el-menu-item>
            <el-menu-item index="/config/list">
              <el-icon class="menu-icon"><Setting /></el-icon>
              <span class="menu-label">配置管理</span>
            </el-menu-item>
            <el-menu-item index="/file/list">
              <el-icon class="menu-icon"><FolderOpened /></el-icon>
              <span class="menu-label">文件管理</span>
            </el-menu-item>
            <el-menu-item index="/log/live">
              <el-icon class="menu-icon"><Document /></el-icon>
              <span class="menu-label">日志中心</span>
            </el-menu-item>
          </el-menu>
          <div class="theme-switcher">
            <el-select v-model="currentTheme" size="small" @change="changeTheme">
              <el-option
                v-for="t in themeOptions"
                :key="t.value"
                :label="t.label"
                :value="t.value"
              />
            </el-select>
          </div>
          <div class="sidebar-footer">
            <el-button size="small" text @click="refreshToken">
              <el-icon style="margin-right: 4px;"><Key /></el-icon>Token
            </el-button>
            <el-button size="small" text @click="goLegacy">
              <el-icon style="margin-right: 4px;"><SwitchButton /></el-icon>旧版
            </el-button>
          </div>
        </div>
      </el-aside>
      <el-main class="main-scroll">
        <router-view />
      </el-main>
    </el-container>
  `,
  data () {
    return {
      currentTheme: localStorage.getItem('fw-theme') || 'default',
      themeOptions: THEME_OPTIONS
    }
  },
  computed: {
    activeMenu () {
      const p = this.$route.path
      if (p.startsWith('/site')) return '/site/list'
      if (p.startsWith('/book')) return '/book/list'
      if (p.startsWith('/config')) return '/config/list'
      if (p.startsWith('/chapter')) return '/book/list'
      if (p.startsWith('/file')) return '/file/list'
      if (p.startsWith('/log')) return '/log/live'
      return '/dashboard'
    }
  },
  created () {
    applyTheme(this.currentTheme)
  },
  methods: {
    selectMenu (path) {
      if (this.$route.path !== path) {
        this.$router.push(path)
      }
    },
    changeTheme (val) {
      applyTheme(val)
    },
    refreshToken () {
      setTokenByPrompt()
    },
    goLegacy () {
      window.location.href = '/main'
    }
  }
}
