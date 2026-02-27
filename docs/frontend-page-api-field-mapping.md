# 前端页面功能、前后端调用与字段映射说明

本文档基于当前代码整理，覆盖 `templates/`、`static/js/` 与对应 `controller/model/vo`。

## 1. 公共机制

### 1.1 通用请求与返回结构
- 前端统一通过 `ajax()`（`src/main/resources/static/js/common/common.js`）发起请求。
- 请求头统一带 `token`（来自 `Cookies` 或 `sessionStorage`，为空时会弹窗输入并 RSA 加密保存）。
- 后端统一返回 `ResponseData<T>`：
  - `code`：状态码（前端约定 `200` 为成功）
  - `message`：提示信息
  - `data`：业务数据

### 1.1.1 鉴权协议（实现替代前端时必须保留）
- 拦截范围：`/api/**`
- 排除范围：`/api/log/**`
- token 获取优先级（后端 `BaseController.getPara`）：
  - 请求头 `token`
  - 请求参数 `token`
  - Cookie `token`
- token 值约定：
  - 明文 token 由用户输入
  - 前端使用固定 RSA 公钥加密后传输（`static/js/utils/rsa.js`）
  - 后端私钥解密后与配置项 `configInfo.token` 比较
- 兼容注意：
  - 旧页面存在 `Token`（大写）请求头写法；若使用新前端，统一发 `token` 更稳妥
  - 值为字符串 `"null"` 会被后端转成 `null`

### 1.2 列表分页映射（Layui Table）
- 前端表格请求字段：
  - `pageName -> pageNum`
  - `limitName -> pageSize`
- 前端表格解析函数 `tableParseData(res)`：
  - `res.code -> table.code`
  - `res.message -> table.msg`
  - `res.data.total -> table.count`
  - `res.data.list -> table.data`
- 对应后端分页模型：
  - 入参：`PageOrder`（`pageNum/pageSize/orderBy/descOrAsc`）
  - 出参：`PageList<T>`（`total/pageNum/pageSize/pages/list/...`）

### 1.3 通用列表页内部逻辑
- 入口函数：`list()`（`common.js`）
  - 双击行：跳转编辑页（`addUrl + id`）
  - 搜索按钮/回车：执行 `search()`
  - 添加按钮：打开新增弹窗
  - 批量删除：收集勾选行 `id`，拼接逗号后调用删除接口
- `search()` 内部逻辑：
  - 从 `.searVal` 取 `name` 作为查询条件
  - 若查询词变化，页码重置为 `1`
  - 触发表格 `table.reload(params, true)`

### 1.4 通用新增/编辑弹窗内部逻辑
- `setAddIframeStyle()`：
  - 自动计算弹窗位置/高度
  - 根据场景设置标题（添加/修改/复制）
- 若 `id != null`：
  - 先调 `modelXxx/{id}` 拉详情
  - 点击（自动触发）`reset` 按钮，`form.val("form", data)` 回填
- 提交：
  - `form.on("submit")` 收集 `data.field`
  - 补上主键（如 `bookid/siteid/configid/chapterid`）
  - `JSON.stringify(field)` 提交到 `saveXxx`
  - 成功后刷新父页并关闭弹窗

### 1.5 页面路由与初始化参数注入
- 页面路由由 `PageController` 返回 Thymeleaf 模板，很多页面依赖服务端注入变量（替代实现必须等价处理）：
  - `/site/add/{id}`：`id`、`optionConfig`
  - `/book/list/{id}`：`siteid`
  - `/book/list`：`siteid = "null"`
  - `/book/add/{id}`：`id`、`optionSite`、`optionConfig`、`optionProxy`
  - `/config/add/{id}`：`id`、`copy`
  - `/chapter/list/{id}`：`book`（包含 `bookid/taskswitch/taskstatus/name` 等）
  - `/chapter/add/{id}`：`id`、`optionBook`、`optionConfig`
  - `/chapter/show/{id}`：`book/chapter/previd/nextid/paragraphList`
  - `/file/list/**`：`path/pathName/list` 或（文件时）`name/form/path`

### 1.6 下拉选项数据来源
- 页面内下拉大多来自服务端模板注入；若改为纯前后端分离，可改为直接调：
  - `GET /api/optionSite`
  - `GET /api/optionConfig`
  - `GET /api/optionBook`
  - `optionProxy` 为 `IBookService.optionProxy()`（当前仅模板注入，无独立 controller 接口）

## 2. 页面清单与映射

## 2.1 首页 `/main`
- 视图：`templates/main.html`
- 脚本：`static/js/main.js`
- 内部逻辑：
  - 菜单跳转到书籍/站点/配置/文件/日志页
  - 点击“文件列表”时触发 `getToken()`，保证后续文件访问带有效 token
  - “设置token”按钮可手动更新 token

## 2.2 站点列表 `/site/list`
- 视图：`templates/site/list.html`
- 脚本：`static/js/site/list.js`
- 后端接口：
  - `POST /api/querySiteList`
  - `GET /api/deleteSite/{id}`
  - 跳转关联页：`/book/list/{siteid}`、`/config/add/{configid}`
- 表格字段映射：
  - `name` -> 名称
  - `configname` -> 配置名称
  - `createtime` -> 创建时间
  - `url` -> 站点链接
- 工具栏逻辑：
  - `showBook`：进入该站点下书籍列表
  - `edit`：打开站点编辑弹窗
  - `del`：删除单条
  - 点击 `configname`：弹出配置编辑页

## 2.3 站点新增/编辑 `/site/add/{id}`
- 视图：`templates/site/add.html`
- 脚本：`static/js/site/add.js`
- 后端接口：
  - `GET /api/modelSite/{id}`
  - `POST /api/saveSite`
- 表单字段 -> 后端 `Site`：
  - `name -> Site.name`
  - `configid -> Site.configid`
  - `url -> Site.url`
  - `siteid`（脚本补充）-> `Site.siteid`
- 内部逻辑：
  - `configid` 做数字校验
  - 编辑态先拉详情再回填

## 2.4 书籍列表 `/book/list`、`/book/list/{siteid}`
- 视图：`templates/book/list.html`
- 脚本：`static/js/book/list.js`
- 后端接口：
  - `POST /api/queryBookList`
  - `GET /api/deleteBook/{id}`
  - `GET /api/downloadBook/{id}`
  - `GET /api/downloadBook2/{id}`
- 请求字段：
  - `name`（搜索词）
  - `siteid`（页面注入，支持按站点过滤）
  - `pageNum/pageSize`（分页）
- 表格字段映射（`BookListReturnVO`）：
  - `name/sitename/configname`
  - `taskstatusname/taskswitchname`
  - `detail/lastgettime/createtime/url/starturl`
- 工具栏逻辑：
  - `showChapter`：进入章节列表
  - `download/download2`：触发文件下载
  - `edit/del`：编辑/删除
  - 点击 `sitename/configname`：弹出对应编辑页

## 2.5 书籍新增/编辑 `/book/add/{id}`
- 视图：`templates/book/add.html`
- 脚本：`static/js/book/add.js`
- 后端接口：
  - `GET /api/modelBook/{id}`
  - `GET /api/modelSite/{siteid}`（站点切换后自动带出配置）
  - `POST /api/saveBook`
- 表单字段 -> 后端 `Book`：
  - `name/url/starturl/siteid/configid/taskswitch/detail`
  - `bookid`（脚本补充）
  - `handlerinfo`（前端组装 JSON 字符串）：
    - `getUtil/titleType/startIndex/delimiter/endType/endCharacter/proxyid`
- 内部逻辑：
  - 编辑态显示 `detail` 区域
  - `handlerinfo` 在编辑态会反序列化并回填到多个控件
  - `titleType/endType` 切换控制字段显隐（分隔符、结束符）
  - 站点变化后自动请求站点详情并写入 `configid`

## 2.6 配置列表 `/config/list`
- 视图：`templates/config/list.html`
- 脚本：`static/js/config/list.js`
- 后端接口：
  - `POST /api/queryConfigList`
  - `GET /api/deleteConfig/{id}`
- 表格字段映射（`Config`）：
  - `name/titlematch/titlelnnum/conmatch/conlnnum/startoffset/endoffset/amatch/nexta`
- 工具栏逻辑：
  - `edit`：编辑
  - `copy`：复制（打开 `/config/add/{id}?copy=true`）
  - `del`：删除

## 2.7 配置新增/编辑/复制 `/config/add/{id}`
- 视图：`templates/config/add.html`
- 脚本：`static/js/config/add.js`
- 后端接口：
  - `GET /api/modelConfig/{id}`
  - `POST /api/saveConfig`
- 表单字段 -> 后端 `Config`：
  - `name/titlematch/titlelnnum/conmatch/conlnnum/startoffset/endoffset/amatch/nexta`
  - `configid`（脚本补充）：
    - 普通编辑：`configid = id`
    - 复制模式：`configid = null`（按新增处理）
- 内部逻辑：
  - 多个数值字段有前端边界校验（如 `endoffset <= 0`）

## 2.8 章节列表 `/chapter/list/{bookid}`
- 视图：`templates/chapter/list.html`
- 脚本：`static/js/chapter/list.js`
- 后端接口：
  - `POST /api/queryChapterList`
  - `GET /api/deleteChapter/{id}`
  - `GET /api/reacquireSingleChapter/{id}`
  - `GET /api/reacquireAllChapter/{bookid}`
  - `GET /api/getFollowUpChapter/{bookid}`
  - `POST /api/getSpecifiedAndFollowUpChapter`
  - `GET /api/queryGetStatus/{bookid}`
  - `GET /api/stopGet/{bookid}`
- 请求字段：
  - 列表查询：`name/bookid/pageNum/pageSize`
  - 指定抓取：`{ bookid, url }`（`SpecifiedFollowUpGetVO`）
- 表格字段映射（`ChapterListReturnVO`）：
  - `name/rawname/bookname/configname/createtime/url`
- 工具栏逻辑：
  - `show`：打开章节正文页
  - `reacquire`：刷新单章
  - `edit/del`：编辑/删除
  - 点击 `bookname/configname`：打开对应编辑页
- 任务相关内部逻辑（重点）：
  - 当 `taskswitch != 0` 时启用任务按钮区
  - 若 `taskstatus != 0`，页面加载即启动定时器，每 5 秒触发一次搜索刷新
  - 搜索时会额外请求 `queryGetStatus` 同步后端状态
  - `stopGet` 停止任务并清理定时器
  - `reacquire/getFollowUp/getSpecifiedAndFollowUp` 调用成功后均启动轮询刷新

## 2.9 章节编辑 `/chapter/add/{id}`
- 视图：`templates/chapter/add.html`
- 脚本：`static/js/chapter/add.js`
- 后端接口：
  - `GET /api/modelChapter/{id}`
  - `POST /api/saveChapter`
- 表单字段 -> 后端 `Chapter`：
  - `name/rawname/bookid/configid/seqid/url`
  - `chapterid`（脚本补充）
- 内部逻辑：
  - `bookid/configid` 在页面中为禁用选择（只读展示）
  - 编辑态详情回填后可修改标题/排序/url 等字段

## 2.10 章节正文 `/chapter/show/{id}`
- 视图：`templates/chapter/show.html`
- 脚本：`static/js/chapter/show.js`
- 数据来源：
  - 页面渲染时已由后端 `PageController.chapterShow` 注入：
    - `book/chapter/previd/nextid/paragraphList`
  - 同源接口模型：`GET /api/queryChapterInfo/{id}`（移动端直接调）
- 内部逻辑：
  - 仅负责上一章/下一章切换（替换 iframe 源地址）

## 2.11 文件列表与播放 `/file/list/**`
- 视图：
  - 目录：`templates/file/list.html`
  - 文件视频：`templates/file/video.html`
- 后端页面入口：`PageController.fileList`
  - 先执行 token 校验（`TokenInterceptor.checkToken()`），失败重定向 `/`
  - 目录返回文件清单；文件返回视频播放页
- 接口调用：
  - 页面内下载/播放资源：`GET /api/file/download/**`
  - API 还提供：`GET /api/file/read?path=...`、`GET /api/file/list/**`
- 内部逻辑：
  - `video.html` 根据窗口宽高按 16:9 自适应播放器高度
  - 默认音量设置为 `0.5`

## 2.12 日志页 `/log/show`
- 视图：`templates/log/show.html`
- 通信：
  - 建立 WebSocket：`ws(s)://{host}/api/log/ws`
  - 连接成功后发送 token 进行鉴权
- 内部逻辑：
  - 按日志级别渲染不同颜色模板
  - 页面最多保留约 200 行，超出会移除最早记录
  - 新日志到达后自动滚动到底部
  - 页面关闭时主动关闭 WebSocket

## 2.13 移动端页面 `/m/list.html`
- 视图：`src/main/resources/static/m/list.html`（Vue + Vonic）
- 子页面与接口：
  - 书籍页：`POST /api/queryBookList`
  - 章节页：`POST /api/queryChapterList`
  - 段落页：`GET /api/queryChapterInfo/{chapterid}`
- 移动端字段映射：
  - 搜索/分页入参：`name/pageNum/pageSize`（章节页额外 `bookid`）
  - 列表返回：`data.list`（书籍含 `bookid/name`，章节含 `chapterid/name`）
  - 段落返回：`book/chapter/previd/nextid/paragraphList`
- 内部逻辑：
  - 路由链路：书籍 -> 章节 -> 段落
  - 上一页/下一页通过本地 `pageNum` 增减后重查
  - 段落页可在上一章/下一章之间连续跳转并实时更新标题

## 3. 后续维护建议
- 新增页面时，优先复用 `common.js` 的列表/弹窗模式，保持交互一致。
- 新增字段时，按以下顺序同步：
  - 表单 `name` / 表格 `field`
  - JS 提交对象与回填逻辑
  - 后端 `Model/VO/Mapper` 字段
- 若修改分页结构，需同步 `tableParseData` 与移动端 `response.data.data` 解析逻辑。

## 4. 可直接复刻的最小接口契约（跨框架实现基线）

以下示例可直接作为 React/Vue/小程序/Flutter 等客户端的联调协议。

### 4.1 列表查询（站点/书籍/配置/章节）
- 请求：
```json
{
  "name": "关键字",
  "siteid": 1,
  "bookid": 2,
  "pageNum": 1,
  "pageSize": 10,
  "orderBy": "createtime",
  "descOrAsc": "Desc"
}
```
- 说明：
  - `siteid` 仅书籍列表需要
  - `bookid` 仅章节列表需要
  - 其余列表只保留 `name/pageNum/pageSize`
- 响应：
```json
{
  "code": 200,
  "message": "",
  "data": {
    "total": 123,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 13,
    "hasPreviousPage": false,
    "hasNextPage": true,
    "list": []
  }
}
```

### 4.2 保存（新增/编辑）
- 站点 `POST /api/saveSite`：
```json
{"siteid": null, "name": "xx", "configid": 1, "url": "https://..."}
```
- 书籍 `POST /api/saveBook`：
```json
{
  "bookid": null,
  "name": "xx",
  "siteid": 1,
  "configid": 2,
  "url": "https://...",
  "starturl": "https://...",
  "taskswitch": 1,
  "handlerinfo": "{\"getUtil\":\"htmlunit\",\"titleType\":0,\"startIndex\":0,\"delimiter\":\"\",\"endType\":0,\"endCharacter\":\"\",\"proxyid\":0}"
}
```
- 配置 `POST /api/saveConfig`：
```json
{
  "configid": null,
  "name": "xx",
  "titlematch": "xpath/css",
  "titlelnnum": 2,
  "conmatch": "xpath/css",
  "conlnnum": 2,
  "startoffset": 0,
  "endoffset": 0,
  "amatch": "xpath/css",
  "nexta": 0
}
```
- 章节 `POST /api/saveChapter`：
```json
{
  "chapterid": null,
  "name": "第1章",
  "rawname": "第一章 ...",
  "bookid": 1,
  "configid": 2,
  "seqid": 1,
  "url": "https://..."
}
```

### 4.3 章节任务控制（状态机）
- 触发接口：
  - 全量重抓：`GET /api/reacquireAllChapter/{bookid}`
  - 抓取后续：`GET /api/getFollowUpChapter/{bookid}`
  - 指定+后续：`POST /api/getSpecifiedAndFollowUpChapter`（`{bookid,url}`）
  - 单章刷新：`GET /api/reacquireSingleChapter/{chapterid}`
  - 查询状态：`GET /api/queryGetStatus/{bookid}`
  - 终止：`GET /api/stopGet/{bookid}`
- 前端建议状态机：
  - `idle`：不轮询
  - `running`：每 5s 查询一次状态并刷新章节列表
  - `stopping`：调用 stop 后等待一次状态确认，转回 `idle`

### 4.4 日志流（WebSocket）
- 建连：`/api/log/ws`
- 首条消息：发送加密 token 字符串
- 收包格式（JSON）字段至少含：
  - `date/level/thread/logger/msg/errorDetail`
- 客户端行为：
  - 最多保留 200 条
  - 新日志自动滚底

## 5. 验收清单（用于判断“仅凭文档可实现”）
- 能完成 token 输入、加密、持久化、附带请求。
- 能实现四类列表页：搜索、分页、批量删除、行内跳转。
- 能实现四类新增编辑页：详情回填、校验、提交、成功后刷新。
- 能实现章节任务流：启动、轮询状态、停止。
- 能实现章节正文前后章切换与移动端同等查询链路。
- 能实现文件浏览/下载/视频播放与日志实时查看。
