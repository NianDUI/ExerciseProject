export default {
    name: "ModernDashboard",
    template: `
    <div class="card">
      <h2>fictionweb 新管理后台</h2>
      <p>使用 Vue3 + Element Plus 重构，覆盖站点、书籍、配置、章节、文件与日志能力。</p>
      <div class="row-gap">
        <el-tag type="success">现代化组件</el-tag>
        <el-tag type="info">分层页面代码</el-tag>
        <el-tag type="warning">兼容现有后端接口</el-tag>
      </div>
      <el-divider />
      <div class="row-gap">
        <el-button type="primary" @click="$router.push('/book/list')">书籍管理</el-button>
        <el-button type="primary" @click="$router.push('/site/list')">站点管理</el-button>
        <el-button type="primary" @click="$router.push('/config/list')">配置管理</el-button>
        <el-button type="primary" @click="$router.push('/file/list')">文件管理</el-button>
        <el-button type="primary" @click="$router.push('/log/live')">日志中心</el-button>
        <el-button type="primary" @click="goMobile">移动端页面</el-button>
        <el-button @click="goIvi">IVI测试频道列表</el-button>
        <el-button @click="goLegacy">旧版首页</el-button>
      </div>
    </div>
  `,
    methods: {
        goMobile() {
            window.location.href = "/modern/mobile";
        },
        goIvi() {
            window.open("http://ivi.bupt.edu.cn", "_blank");
        },
        goLegacy() {
            window.location.href = "/main";
        }
    }
};
