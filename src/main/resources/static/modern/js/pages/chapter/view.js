import {get} from "../../common/http.js";

export default {
    name: "ChapterViewPage",
    template: `
    <div class="card">
      <div class="toolbar">
        <el-button @click="$router.back()">返回</el-button>
        <el-button :disabled="!previd" @click="loadById(previd)">上一章</el-button>
        <el-button :disabled="!nextid" @click="loadById(nextid)">下一章</el-button>
      </div>
      <h3>{{ chapter.name }} <span v-if="book.name">- {{ book.name }}</span></h3>
      <el-divider />
      <div class="chapter-content">
        <p v-for="item in paragraphList" :key="item.seqid">{{ item.content }}</p>
      </div>
      <div class="toolbar" style="margin-top: 16px;">
        <el-button @click="$router.back()">返回</el-button>
        <el-button :disabled="!previd" @click="loadById(previd)">上一章</el-button>
        <el-button :disabled="!nextid" @click="loadById(nextid)">下一章</el-button>
      </div>
    </div>
  `,
    data() {
        return {
            book: {},
            chapter: {},
            paragraphList: [],
            previd: null,
            nextid: null
        };
    },
    async mounted() {
        await this.loadById(Number(this.$route.params.id));
    },
    methods: {
        scrollToTop() {
            const container = document.querySelector(".main-scroll");
            if (container) {
                container.scrollTo({ top: 0, left: 0, behavior: "auto" });
                container.scrollTop = 0;
            }
            document.documentElement.scrollTop = 0;
            document.body.scrollTop = 0;
            window.scrollTo({ top: 0, left: 0, behavior: "auto" });
        },
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
            if (this.chapter.chapterid) {
                this.$router.replace(`/chapter/view/${this.chapter.chapterid}`);
            }
            this.$nextTick(() => {
                this.scrollToTop();
            });
        }
    }
};
