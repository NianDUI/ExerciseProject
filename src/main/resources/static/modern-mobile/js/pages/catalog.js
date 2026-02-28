import {normalizePageResult, post} from "../../../modern/js/common/http.js";
import {getBookProgress} from "../common/storage.js";

export default {
    name: "MobileCatalogPage",
    template: `
    <div class="mobile-page">
      <section class="mobile-section">
        <div class="catalog-head">
          <div>
            <p class="hero-kicker">目录</p>
            <h2 class="catalog-title">{{ bookName }}</h2>
          </div>
          <el-button round @click="$router.push('/')">回书城</el-button>
        </div>

        <div v-if="progress" class="continue-card compact">
          <div>
            <div class="continue-book">上次读到 {{ progress.chapterName }}</div>
            <div class="continue-meta">{{ formatTime(progress.updatedAt) }}</div>
          </div>
          <el-button type="primary" round @click="resumeRead">继续</el-button>
        </div>

        <div class="search-box">
          <el-input v-model="query.name" clearable placeholder="搜索章节" @keyup.enter="search" />
          <el-segmented v-model="sortMode" :options="sortOptions" />
        </div>

        <div v-loading="loading" class="chapter-list">
          <button
            v-for="item in sortedList"
            :key="item.chapterid"
            class="chapter-item"
            :class="{ active: progress && progress.chapterId === item.chapterid }"
            @click="openReader(item)">
            <span class="chapter-name">{{ item.name }}</span>
            <small class="chapter-sub">{{ item.rawname || item.createtime || '' }}</small>
          </button>
        </div>

        <div class="load-more-wrap">
          <el-button :loading="loadingMore" :disabled="!hasNextPage" round @click="loadMore">
            {{ hasNextPage ? '加载更多章节' : '已经到底了' }}
          </el-button>
        </div>
      </section>
    </div>
  `,
    data() {
        return {
            loading: false,
            loadingMore: false,
            list: [],
            hasNextPage: false,
            sortMode: "asc",
            sortOptions: [
                { label: "正序", value: "asc" },
                { label: "倒序", value: "desc" }
            ],
            query: {
                name: "",
                bookid: null,
                pageNum: 1,
                pageSize: 20
            }
        };
    },
    computed: {
        bookId() {
            return Number(this.$route.params.bookid);
        },
        bookName() {
            return this.$route.query.name || (this.progress && this.progress.bookName) || "章节目录";
        },
        progress() {
            return getBookProgress(this.bookId);
        },
        sortedList() {
            const arr = (this.list || []).slice();
            if (this.sortMode === "desc") {
                arr.reverse();
            }
            return arr;
        }
    },
    mounted() {
        this.query.bookid = this.bookId;
        this.load(false);
    },
    watch: {
        "$route.params.bookid"() {
            this.query.bookid = this.bookId;
            this.query.pageNum = 1;
            this.list = [];
            this.load(false);
        }
    },
    methods: {
        async load(append) {
            if (append) {
                this.loadingMore = true;
            } else {
                this.loading = true;
            }
            try {
                const data = await post("/api/queryChapterList", this.query);
                const page = normalizePageResult(data);
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
        openReader(item) {
            this.$router.push({
                path: `/reader/${item.chapterid}`,
                query: {
                    bookId: String(this.bookId),
                    bookName: this.bookName
                }
            });
        },
        resumeRead() {
            if (!this.progress || !this.progress.chapterId) {
                return;
            }
            this.$router.push({
                path: `/reader/${this.progress.chapterId}`,
                query: {
                    bookId: String(this.bookId),
                    bookName: this.bookName
                }
            });
        },
        formatTime(value) {
            if (!value) {
                return "-";
            }
            return value.replace("T", " ").slice(0, 16);
        }
    }
};
