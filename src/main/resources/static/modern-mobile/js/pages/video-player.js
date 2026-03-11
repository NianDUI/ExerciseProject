import {encodePath, get} from "../../../modern/js/common/http.js";
import {createVideoPlayer, disposeVideoPlayer} from "../../../modern/js/common/video-player.js";

export default {
    name: "MobileVideoPlayerPage",
    template: `
    <div class="video-player-page mobile-fit-page">
      <div ref="topbarRef" class="video-player-topbar">
        <button class="ghost-btn" @click="$router.back()">返回</button>
        <div class="video-player-title">{{ title }}</div>
        <a class="ghost-btn link-btn" :href="downloadUrl">下载</a>
      </div>
      <div ref="pageRef" class="video-stage mobile-fit-stage" :style="stageStyle">
        <video
          id="mobile-video-player"
          class="video-js vjs-default-skin vjs-big-play-centered"
          playsinline
          webkit-playsinline
          x5-playsinline
        ></video>
      </div>
      <div ref="panelRef" class="video-player-panel">
        <div class="video-player-meta">
          <span class="meta-chip">{{ videoType }}</span>
          <span class="meta-chip">{{ playbackLabel }}</span>
        </div>
        <div v-if="statusMessage && !errorMessage" class="video-error-box">
          <div>{{ statusMessage }}</div>
        </div>
        <div v-if="errorMessage" class="video-error-box">
          <div>{{ errorMessage }}</div>
          <button class="ghost-btn" @click="reloadPlayer">重试播放</button>
        </div>
      </div>
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
            playerWidth: 320,
            playerHeight: 260,
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
            return this.$route.query.name || "视频播放";
        },
        videoType() {
            return this.resolvedVideoType || this.$route.query.type || "video/mp4";
        },
        videoUrl() {
            return this.resolvedVideoUrl || this.downloadUrl;
        },
        downloadUrl() {
            return this.rawPath
                ? `/api/file/downloadByPath?path=${encodeURIComponent(this.rawPath)}`
                : `/api/file/download/${encodePath(this.encodedPath)}`;
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
            this.player = createVideoPlayer("mobile-video-player", {
                source: {
                    src: this.videoUrl,
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
            if (this.$refs.topbarRef) {
                this.resizeObserver.observe(this.$refs.topbarRef);
            }
            if (this.$refs.panelRef) {
                this.resizeObserver.observe(this.$refs.panelRef);
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
            const viewportWidth = window.innerWidth || document.documentElement.clientWidth || 0;
            const topbarHeight = this.$refs.topbarRef ? this.$refs.topbarRef.offsetHeight : 0;
            const panelHeight = this.$refs.panelRef ? this.$refs.panelRef.offsetHeight : 0;
            const verticalGap = 12;
            const maxHeight = Math.max(220, viewportHeight - topbarHeight - panelHeight - verticalGap);
            const maxWidth = Math.max(220, viewportWidth);
            const aspectRatio = meta && meta.aspectRatio ? meta.aspectRatio : 16 / 9;
            const fitByWidthHeight = Math.round(maxWidth / aspectRatio);
            if (fitByWidthHeight <= maxHeight) {
                this.playerWidth = maxWidth;
                this.playerHeight = Math.max(220, fitByWidthHeight);
                return;
            }
            this.playerHeight = maxHeight;
            this.playerWidth = Math.max(180, Math.round(maxHeight * aspectRatio));
        },
        destroyPlayer() {
            disposeVideoPlayer(this.player);
            this.player = null;
        }
    }
};
