import {encodePath, get} from "../../common/http.js";
import {createVideoPlayer, disposeVideoPlayer} from "../../common/video-player.js";

export default {
    name: "FilePlayerPage",
    template: `
    <div class="card video-card pc-fit-page">
      <div ref="toolbarRef" class="toolbar">
        <el-button @click="$router.back()">返回</el-button>
        <el-tag type="info" effect="dark">{{ title }}</el-tag>
        <el-tag>{{ videoType }}</el-tag>
        <el-tag type="success">{{ playbackLabel }}</el-tag>
        <el-button type="primary" @click="download">下载视频</el-button>
      </div>

        <div ref="pageRef" class="video-stage pc pc-fit-stage" :style="stageStyle">
        <video id="pc-video-player" class="video-js vjs-default-skin vjs-big-play-centered"></video>
      </div>

      <el-alert
        v-if="statusMessage && !errorMessage"
        :closable="false"
        type="info"
        show-icon
        :title="statusMessage"
        style="margin-top: 16px;"
      />

      <el-alert
        ref="alertRef"
        v-if="errorMessage"
        :closable="false"
        type="error"
        show-icon
        :title="errorMessage"
        style="margin-top: 16px;">
        <template #default>
          <el-button size="small" @click="reloadPlayer">重试播放</el-button>
        </template>
      </el-alert>
    </div>
  `,
    data() {
        return {
            player: null,
            errorMessage: "",
            statusMessage: "",
            playbackLabel: "倍速 1x",
            resolvedVideoUrl: "",
            resolvedVideoType: "video/mp4",
            sourceStatus: "",
            pollTimer: null,
            playerWidth: 640,
            playerHeight: 420,
            resizeObserver: null
        };
    },
    computed: {
        rawPath() {
            return this.$route.query.rawPath || "";
        },
        encodedPath() {
            return this.$route.query.path || "";
        },
        title() {
            return this.$route.query.name || "视频播放器";
        },
        videoType() {
            return this.resolvedVideoType || this.$route.query.type || "video/mp4";
        },
        stageStyle() {
            return {
                "--player-stage-width": `${this.playerWidth}px`,
                "--player-stage-height": `${this.playerHeight}px`
            };
        }
    },
    mounted() {
        this.$nextTick(() => {
            this.updateStageByViewport();
            this.initPlayer();
            window.addEventListener("resize", this.updateStageByViewport);
            this.initResizeObserver();
        });
    },
    beforeUnmount() {
        window.removeEventListener("resize", this.updateStageByViewport);
        this.destroyResizeObserver();
        this.stopPolling();
        this.destroyPlayer();
    },
    methods: {
        async initPlayer() {
            this.destroyPlayer();
            this.stopPolling();
            this.errorMessage = "";
            this.statusMessage = "正在准备播放地址，部分格式首次播放需要转码，请稍候...";
            try {
                const source = this.rawPath
                    ? await get(`/api/file/video/sourceByPath?path=${encodeURIComponent(this.rawPath)}`)
                    : await get(`/api/file/video/source/${encodePath(this.encodedPath)}`);
                this.applySource(source);
            } catch (e) {
                this.statusMessage = "";
                this.errorMessage = e.message || "获取播放地址失败";
                return;
            }
            if (this.sourceStatus === "processing") {
                this.startPolling();
                return;
            }
            if (this.sourceStatus !== "ready" || !this.resolvedVideoUrl) {
                this.statusMessage = "";
                this.errorMessage = this.errorMessage || "未获取到可播放地址";
                return;
            }
            this.mountPlayer();
        },
        mountPlayer() {
            this.destroyPlayer();
            this.player = createVideoPlayer("pc-video-player", {
                source: {
                    src: this.resolvedVideoUrl,
                    type: this.videoType
                },
                onError: (message) => {
                    this.errorMessage = message;
                },
                onReady: (player) => {
                    this.statusMessage = "";
                    player.on("ratechange", () => {
                        this.playbackLabel = `倍速 ${player.playbackRate()}x`;
                    });
                },
                onLoadedMetadata: (meta) => {
                    this.updateStageHeight(meta);
                }
            });
        },
        applySource(source) {
            this.sourceStatus = source && source.status ? source.status : "";
            this.resolvedVideoUrl = source && source.sourceUrl ? source.sourceUrl : "";
            this.resolvedVideoType = source && source.mimeType ? source.mimeType : "video/mp4";
            if (this.sourceStatus === "ready") {
                this.statusMessage = "";
                this.errorMessage = "";
                return;
            }
            if (this.sourceStatus === "failed") {
                this.statusMessage = "";
                this.errorMessage = source && source.message ? source.message : "视频转码失败";
                return;
            }
            this.statusMessage = source && source.message ? source.message : "视频转码中，请稍候...";
        },
        startPolling() {
            this.stopPolling();
            this.pollTimer = window.setTimeout(async () => {
                try {
                    const source = this.rawPath
                        ? await get(`/api/file/video/sourceByPath?path=${encodeURIComponent(this.rawPath)}`)
                        : await get(`/api/file/video/source/${encodePath(this.encodedPath)}`);
                    this.applySource(source);
                    if (this.sourceStatus === "ready" && this.resolvedVideoUrl) {
                        this.stopPolling();
                        this.mountPlayer();
                        return;
                    }
                    if (this.sourceStatus === "processing") {
                        this.startPolling();
                    }
                } catch (e) {
                    this.stopPolling();
                    this.statusMessage = "";
                    this.errorMessage = e.message || "获取播放地址失败";
                }
            }, 3000);
        },
        stopPolling() {
            if (this.pollTimer) {
                window.clearTimeout(this.pollTimer);
                this.pollTimer = null;
            }
        },
        reloadPlayer() {
            this.initPlayer();
        },
        initResizeObserver() {
            if (!window.ResizeObserver) {
                return;
            }
            this.destroyResizeObserver();
            this.resizeObserver = new window.ResizeObserver(() => {
                this.updateStageByViewport();
            });
            if (this.$refs.toolbarRef) {
                this.resizeObserver.observe(this.$refs.toolbarRef);
            }
            const alertEl = this.$refs.alertRef && this.$refs.alertRef.$el ? this.$refs.alertRef.$el : null;
            if (alertEl) {
                this.resizeObserver.observe(alertEl);
            }
        },
        destroyResizeObserver() {
            if (this.resizeObserver) {
                this.resizeObserver.disconnect();
                this.resizeObserver = null;
            }
        },
        updateStageByViewport() {
            this.updateStageHeight();
        },
        updateStageHeight(meta) {
            const viewportHeight = window.innerHeight || document.documentElement.clientHeight || 0;
            const stageWidth = this.$refs.pageRef ? this.$refs.pageRef.clientWidth : (window.innerWidth || 0);
            const toolbarHeight = this.$refs.toolbarRef ? this.$refs.toolbarRef.offsetHeight : 0;
            const alertEl = this.$refs.alertRef && this.$refs.alertRef.$el ? this.$refs.alertRef.$el : null;
            const alertHeight = alertEl ? alertEl.offsetHeight : 0;
            const cardPadding = 32;
            const maxHeight = Math.max(320, viewportHeight - toolbarHeight - alertHeight - cardPadding - 92);
            const maxWidth = Math.max(320, stageWidth || 0);
            const aspectRatio = meta && meta.aspectRatio ? meta.aspectRatio : 16 / 9;
            const fitByWidthHeight = Math.round(maxWidth / aspectRatio);
            if (fitByWidthHeight <= maxHeight) {
                this.playerWidth = maxWidth;
                this.playerHeight = Math.max(320, fitByWidthHeight);
                return;
            }
            this.playerHeight = maxHeight;
            this.playerWidth = Math.max(220, Math.round(maxHeight * aspectRatio));
        },
        download() {
            window.location.href = this.rawPath
                ? `/api/file/downloadByPath?path=${encodeURIComponent(this.rawPath)}`
                : `/api/file/download/${encodePath(this.encodedPath)}`;
        },
        destroyPlayer() {
            disposeVideoPlayer(this.player);
            this.player = null;
        }
    }
};
