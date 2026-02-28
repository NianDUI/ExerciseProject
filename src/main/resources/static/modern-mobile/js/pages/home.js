import {normalizePageResult, post} from "../../../modern/js/common/http.js";
import {getBookProgress, getRecentProgressList} from "../common/storage.js";

export default {
    name: "MobileHomePage",
    template: `
    <div class="mobile-page">
      <section class="hero-panel">
        <div>
          <p class="hero-kicker">fictionweb Mobile</p>
          <h2 class="hero-title">随手找书，继续阅读</h2>
          <p class="hero-desc">优先保留手机上最常用的阅读链路，搜索更轻，目录更快，正文更稳。</p>
        </div>
      </section>

      <section v-if="continueCard" class="mobile-section">
        <div class="section-head">
          <h3>继续阅读</h3>
        </div>
        <div class="continue-card">
          <div>
            <div class="continue-book">{{ continueCard.bookName }}</div>
            <div class="continue-chapter">{{ continueCard.chapterName }}</div>
            <div class="continue-meta">上次阅读 {{ formatTime(continueCard.updatedAt) }}</div>
          </div>
          <el-button type="primary" round @click="resumeRead(continueCard)">继续</el-button>
        </div>
      </section>

      <section class="mobile-section">
        <div class="section-head">
          <h3>书城</h3>
          <span class="section-desc">共 {{ total }} 本</span>
        </div>
        <div class="search-box">
          <el-input v-model="query.name" clearable placeholder="搜索书名" @keyup.enter="search" />
          <el-button type="primary" @click="search">搜索</el-button>
        </div>
        <div class="book-grid" v-loading="loading">
          <article v-for="item in list" :key="item.bookid" class="book-card">
            <div class="book-cover">{{ getInitial(item.name) }}</div>
            <div class="book-main">
              <div class="book-title">{{ item.name }}</div>
              <div class="book-meta">{{ item.sitename || '未设置站点' }} · {{ item.configname || '未设置配置' }}</div>
              <div class="book-meta">状态 {{ item.taskstatusname || '-' }} / {{ item.taskswitchname || '-' }}</div>
              <div class="book-meta">更新时间 {{ item.lastgettime || item.createtime || '-' }}</div>
              <div class="book-actions">
                <el-button round @click="openCatalog(item)">查看目录</el-button>
                <el-button type="primary" round @click="continueOrCatalog(item)">
                  {{ hasProgress(item.bookid) ? '继续阅读' : '开始阅读' }}
                </el-button>
              </div>
            </div>
          </article>
        </div>
        <div class="load-more-wrap">
          <el-button :loading="loadingMore" :disabled="!hasNextPage" round @click="loadMore">
            {{ hasNextPage ? '加载更多' : '没有更多了' }}
          </el-button>
        </div>
      </section>

      <section v-if="recentList.length" class="mobile-section">
        <div class="section-head">
          <h3>最近阅读</h3>
        </div>
        <div class="recent-list">
          <button v-for="item in recentList" :key="item.bookId" class="recent-item" @click="resumeRead(item)">
            <span>{{ item.bookName }}</span>
            <small>{{ item.chapterName }}</small>
          </button>
        </div>
      </section>
    </div>
  `,
    data() {
        return {
            loading: false,
            loadingMore: false,
            total: 0,
            list: [],
            hasNextPage: false,
            query: {
                name: "",
                pageNum: 1,
                pageSize: 10
            }
        };
    },
    computed: {
        continueCard() {
            return this.recentList.length ? this.recentList[0] : null;
        },
        recentList() {
            return getRecentProgressList(4);
        }
    },
    mounted() {
        this.load();
    },
    methods: {
        async load(append) {
            if (append) {
                this.loadingMore = true;
            } else {
                this.loading = true;
            }
            try {
                const data = await post("/api/queryBookList", this.query);
                const page = normalizePageResult(data);
                this.total = page.total;
                this.hasNextPage = page.hasNextPage;
                this.list = append ? this.list.concat(page.list || []) : (page.list || []);
            } finally {
                this.loading = false;
                this.loadingMore = false;
            }
        },
        search() {
            this.query.pageNum = 1;
            this.load(false);
        },
        loadMore() {
            if (!this.hasNextPage) {
                return;
            }
            this.query.pageNum += 1;
            this.load(true);
        },
        openCatalog(item) {
            this.$router.push({
                path: `/catalog/${item.bookid}`,
                query: { name: item.name || "" }
            });
        },
        continueOrCatalog(item) {
            const progress = getBookProgress(item.bookid);
            if (progress && progress.chapterId) {
                this.resumeRead(progress);
                return;
            }
            this.openCatalog(item);
        },
        resumeRead(progress) {
            if (!progress || !progress.chapterId) {
                return;
            }
            this.$router.push({
                path: `/reader/${progress.chapterId}`,
                query: {
                    bookId: progress.bookId || "",
                    bookName: progress.bookName || ""
                }
            });
        },
        hasProgress(bookId) {
            return !!getBookProgress(bookId);
        },
        getInitial(name) {
            return (name || "?").slice(0, 1);
        },
        formatTime(value) {
            if (!value) {
                return "-";
            }
            return value.replace("T", " ").slice(0, 16);
        }
    }
};
