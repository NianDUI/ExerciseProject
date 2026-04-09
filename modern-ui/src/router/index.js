import { createRouter, createWebHashHistory } from 'vue-router'

import DashboardPage from '../pages/dashboard.js'
import SiteListPage from '../pages/site/list.js'
import SiteFormPage from '../pages/site/form.js'
import BookListPage from '../pages/book/list.js'
import BookFormPage from '../pages/book/form.js'
import ConfigListPage from '../pages/config/list.js'
import ConfigFormPage from '../pages/config/form.js'
import ChapterListPage from '../pages/chapter/list.js'
import ChapterFormPage from '../pages/chapter/form.js'
import ChapterViewPage from '../pages/chapter/view.js'
import FileListPage from '../pages/file/list.js'
import FilePlayerPage from '../pages/file/player.js'
import FileImageViewerPage from '../pages/file/image-viewer.js'
import FileTextViewerPage from '../pages/file/text-viewer.js'
import LogLivePage from '../pages/log/live.js'

const routes = [
  { path: '/', redirect: '/dashboard' },
  { path: '/dashboard', component: DashboardPage, meta: { title: '仪表盘' } },
  { path: '/site/list', component: SiteListPage, meta: { title: '站点管理' } },
  { path: '/site/form/:id', component: SiteFormPage, meta: { title: '站点编辑' } },
  { path: '/book/list', component: BookListPage, meta: { title: '书籍管理' } },
  { path: '/book/list/:siteid', component: BookListPage, meta: { title: '书籍管理' } },
  { path: '/book/form/:id', component: BookFormPage, meta: { title: '书籍编辑' } },
  { path: '/config/list', component: ConfigListPage, meta: { title: '配置管理' } },
  { path: '/config/form/:id', component: ConfigFormPage, meta: { title: '配置编辑' } },
  { path: '/chapter/list/:bookid', component: ChapterListPage, meta: { title: '章节管理' } },
  { path: '/chapter/form/:id', component: ChapterFormPage, meta: { title: '章节编辑' } },
  { path: '/chapter/view/:id', component: ChapterViewPage, meta: { title: '章节内容' } },
  { path: '/file/list/:pathMatch(.*)*', component: FileListPage, meta: { title: '文件管理' } },
  { path: '/file/player', component: FilePlayerPage, meta: { title: '视频播放器' } },
  { path: '/file/image', component: FileImageViewerPage, meta: { title: '图片查看' } },
  { path: '/file/text', component: FileTextViewerPage, meta: { title: '文本查看' } },
  { path: '/log/live', component: LogLivePage, meta: { title: '日志中心' } }
]

export default createRouter({
  history: createWebHashHistory(),
  routes
})
