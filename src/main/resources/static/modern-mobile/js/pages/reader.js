import {get} from "../../../modern/js/common/http.js";
import {getBookProgress, getReaderPreference, saveBookProgress, saveReaderPreference} from "../common/storage.js";

const THEMES = {
    paper: { label: "米白", className: "theme-paper" },
    mint: { label: "浅绿", className: "theme-mint" },
    night: { label: "深色", className: "theme-night" }
};

export default {
    name: "MobileReaderPage",
    template: `
    <div class="reader-page" :class="theme.className">
      <header class="reader-topbar" :class="{ hidden: chromeHidden }">
        <button class="ghost-btn" @click="goBack">目录</button>
        <div class="reader-title-wrap">
          <div class="reader-book">{{ book.name || bookNameFromQuery || '正在阅读' }}</div>
          <div class="reader-chapter">{{ chapter.name || '章节内容' }}</div>
        </div>
        <button class="ghost-btn" @click="drawerVisible = true">设置</button>
      </header>

      <main ref="scrollRef" class="reader-content" @click="toggleChrome" @scroll="saveScrollProgress">
        <div class="reader-article" :style="readerStyle">
          <p v-for="item in paragraphList" :key="item.seqid">{{ item.content }}</p>
        </div>
      </main>

      <footer class="reader-toolbar" :class="{ hidden: chromeHidden }">
        <button class="toolbar-btn" :disabled="!previd" @click="loadById(previd)">上一章</button>
        <button class="toolbar-btn" @click="goBack">目录</button>
        <button class="toolbar-btn" :disabled="!nextid" @click="loadById(nextid)">下一章</button>
      </footer>

      <el-drawer v-model="drawerVisible" direction="btt" size="360px" title="阅读设置">
        <div class="setting-group">
          <span>字号</span>
          <el-slider v-model="preference.fontSize" :min="16" :max="28" @change="savePreference" />
        </div>
        <div class="setting-group">
          <span>行高</span>
          <el-slider v-model="preference.lineHeight" :min="1.5" :max="2.4" :step="0.05" @change="savePreference" />
        </div>
        <div class="setting-group">
          <span>主题</span>
          <div class="theme-pills">
            <button
              v-for="(item, key) in themes"
              :key="key"
              type="button"
              class="theme-pill"
              :class="{ active: preference.theme === key }"
              @click="changeTheme(key)">
              {{ item.label }}
            </button>
          </div>
        </div>
      </el-drawer>
    </div>
  `,
    data() {
        return {
            drawerVisible: false,
            chromeHidden: false,
            book: {},
            chapter: {},
            paragraphList: [],
            previd: null,
            nextid: null,
            themes: THEMES,
            preference: getReaderPreference()
        };
    },
    computed: {
        chapterId() {
            return Number(this.$route.params.id);
        },
        bookNameFromQuery() {
            return this.$route.query.bookName || "";
        },
        theme() {
            return THEMES[this.preference.theme] || THEMES.paper;
        },
        readerStyle() {
            return {
                fontSize: `${this.preference.fontSize}px`,
                lineHeight: String(this.preference.lineHeight)
            };
        }
    },
    async mounted() {
        await this.loadById(this.chapterId);
    },
    watch: {
        "$route.params.id"(value) {
            this.loadById(Number(value));
        }
    },
    methods: {
        async loadById(id) {
            if (!id) {
                return;
            }
            const data = await get(`/api/queryChapterInfo/${id}`);
            this.book = data.book || {};
            this.chapter = data.chapter || {};
            this.paragraphList = data.paragraphList || [];
            this.previd = data.previd;
            this.nextid = data.nextid;
            this.$nextTick(() => {
                this.restoreScroll();
                this.persistProgress();
            });
        },
        goBack() {
            const bookId = this.book.bookid || this.$route.query.bookId;
            const name = this.book.name || this.$route.query.bookName || "";
            if (bookId) {
                this.$router.push({ path: `/catalog/${bookId}`, query: { name } });
                return;
            }
            this.$router.push("/");
        },
        toggleChrome() {
            this.chromeHidden = !this.chromeHidden;
        },
        savePreference() {
            saveReaderPreference(this.preference);
        },
        changeTheme(key) {
            this.preference.theme = key;
            this.savePreference();
        },
        persistProgress() {
            saveBookProgress({
                bookId: this.book.bookid || Number(this.$route.query.bookId),
                bookName: this.book.name || this.$route.query.bookName || "",
                chapterId: this.chapter.chapterid || this.chapterId,
                chapterName: this.chapter.name || "",
                scrollTop: this.$refs.scrollRef ? this.$refs.scrollRef.scrollTop : 0
            });
        },
        saveScrollProgress() {
            this.persistProgress();
        },
        restoreScroll() {
            const progress = getBookProgress(this.book.bookid || Number(this.$route.query.bookId));
            if (!this.$refs.scrollRef) {
                return;
            }
            if (!progress || progress.chapterId !== (this.chapter.chapterid || this.chapterId)) {
                this.$refs.scrollRef.scrollTop = 0;
                return;
            }
            this.$refs.scrollRef.scrollTop = progress.scrollTop || 0;
        }
    }
};
