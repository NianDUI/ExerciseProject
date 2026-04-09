import axios from 'axios'
import Cookies from 'js-cookie'
import { encodePath } from '../../common/http.js'

export default {
  name: 'FileTextViewerPage',
  template: `
    <div class="card text-viewer-card">
      <div class="toolbar">
        <el-button @click="$router.back()">返回</el-button>
        <el-tag type="info" effect="dark">{{ title }}</el-tag>
        <el-button type="primary" @click="download">下载文本</el-button>
      </div>
      <div class="pc-text-wrap" v-loading="loading">
        <pre class="text-preview pc-text-view">{{ textContent }}</pre>
      </div>
    </div>
  `,
  data () {
    return {
      loading: false,
      textContent: ''
    }
  },
  computed: {
    rawPath () {
      return this.$route.query.path || ''
    },
    title () {
      return this.$route.query.name || '文本查看'
    },
    downloadUrl () {
      return `/api/file/download/${encodePath(this.rawPath)}`
    }
  },
  mounted () {
    this.load()
  },
  methods: {
    async load () {
      this.loading = true
      try {
        const token = Cookies.get('token') || sessionStorage.getItem('token') || ''
        const res = await axios.get(`/api/file/read?path=${encodeURIComponent(this.rawPath)}`, {
          headers: { token },
          responseType: 'text'
        })
        this.textContent = typeof res.data === 'string' ? res.data : ''
      } finally {
        this.loading = false
      }
    },
    download () {
      window.location.href = this.downloadUrl
    }
  }
}
