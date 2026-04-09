import { get } from '../common/http.js'

export default {
  name: 'ModernDashboard',
  template: `
    <div>
      <div class="dashboard-header">
        <h2>FictionWeb 管理后台</h2>
        <p>基于 Vue 3 + Element Plus 构建，覆盖站点、书籍、配置、章节、文件与日志管理能力。</p>
      </div>

      <div class="stat-cards">
        <div class="stat-card" @click="$router.push('/site/list')">
          <div class="stat-icon blue"><el-icon :size="22"><Monitor /></el-icon></div>
          <div class="stat-info">
            <div class="stat-label">站点</div>
            <div class="stat-value">{{ stats.sites ?? '-' }}</div>
          </div>
        </div>
        <div class="stat-card" @click="$router.push('/book/list')">
          <div class="stat-icon green"><el-icon :size="22"><Reading /></el-icon></div>
          <div class="stat-info">
            <div class="stat-label">书籍</div>
            <div class="stat-value">{{ stats.books ?? '-' }}</div>
          </div>
        </div>
        <div class="stat-card" @click="$router.push('/config/list')">
          <div class="stat-icon orange"><el-icon :size="22"><Setting /></el-icon></div>
          <div class="stat-info">
            <div class="stat-label">配置</div>
            <div class="stat-value">{{ stats.configs ?? '-' }}</div>
          </div>
        </div>
        <div class="stat-card" @click="$router.push('/file/list')">
          <div class="stat-icon purple"><el-icon :size="22"><FolderOpened /></el-icon></div>
          <div class="stat-info">
            <div class="stat-label">文件管理</div>
            <div class="stat-value"><el-icon :size="16"><Right /></el-icon></div>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="quick-nav">
          <h3>快速导航</h3>
          <div class="quick-nav-grid">
            <div class="quick-nav-item" @click="$router.push('/book/list')">
              <el-icon class="nav-icon" :size="24"><Reading /></el-icon>
              <span class="nav-label">书籍管理</span>
            </div>
            <div class="quick-nav-item" @click="$router.push('/site/list')">
              <el-icon class="nav-icon" :size="24"><Monitor /></el-icon>
              <span class="nav-label">站点管理</span>
            </div>
            <div class="quick-nav-item" @click="$router.push('/config/list')">
              <el-icon class="nav-icon" :size="24"><Setting /></el-icon>
              <span class="nav-label">配置管理</span>
            </div>
            <div class="quick-nav-item" @click="$router.push('/file/list')">
              <el-icon class="nav-icon" :size="24"><FolderOpened /></el-icon>
              <span class="nav-label">文件管理</span>
            </div>
            <div class="quick-nav-item" @click="$router.push('/log/live')">
              <el-icon class="nav-icon" :size="24"><Document /></el-icon>
              <span class="nav-label">日志中心</span>
            </div>
            <div class="quick-nav-item" @click="goMobile">
              <el-icon class="nav-icon" :size="24"><Cellphone /></el-icon>
              <span class="nav-label">移动端</span>
            </div>
            <div class="quick-nav-item" @click="goLegacy">
              <el-icon class="nav-icon" :size="24"><SwitchButton /></el-icon>
              <span class="nav-label">旧版首页</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  data () {
    return {
      stats: { sites: null, books: null, configs: null }
    }
  },
  mounted () {
    this.loadStats()
  },
  methods: {
    async loadStats () {
      try {
        const [sites, books, configs] = await Promise.all([
          get('/api/optionSite').catch(() => []),
          get('/api/optionBook').catch(() => []),
          get('/api/optionConfig').catch(() => [])
        ])
        this.stats.sites = Array.isArray(sites) ? sites.length : 0
        this.stats.books = Array.isArray(books) ? books.length : 0
        this.stats.configs = Array.isArray(configs) ? configs.length : 0
      } catch (e) {
        // stats are optional
      }
    },
    goMobile () {
      window.location.href = '/modern/mobile'
    },
    goLegacy () {
      window.location.href = '/main'
    }
  }
}
