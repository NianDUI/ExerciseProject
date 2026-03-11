export function getVideoMimeType(name) {
    const lower = String(name || "").toLowerCase();
    if (lower.endsWith(".m3u8")) {
        return "application/x-mpegURL";
    }
    if (lower.endsWith(".webm")) {
        return "video/webm";
    }
    return "video/mp4";
}

export function createVideoPlayer(targetId, options) {
    if (!window.videojs) {
        return null;
    }
    const source = options && options.source ? options.source : null;
    const isM3u8 = !!(source && source.type === "application/x-mpegURL");
    const player = window.videojs(targetId, {
        autoplay: false,
        controls: true,
        preload: "auto",
        fluid: true,
        responsive: true,
        playbackRates: [0.75, 1, 1.25, 1.5, 2],
        html5: {
            vhs: {
                enableLowInitialPlaylist: true,
                smoothQualityChange: true,
                overrideNative: isM3u8 && !window.videojs.browser.IS_SAFARI
            },
            nativeAudioTracks: false,
            nativeVideoTracks: false
        },
        controlBar: {
            playToggle: true,
            currentTimeDisplay: true,
            timeDivider: true,
            durationDisplay: true,
            remainingTimeDisplay: true,
            progressControl: true,
            playbackRateMenuButton: true,
            fullscreenToggle: true,
            pictureInPictureToggle: false,
            volumePanel: { inline: false }
        }
    });
    if (source) {
        player.src(source);
    }
    player.ready(() => {
        player.volume(0.6);
    });
    if (options && typeof options.onError === "function") {
        player.on("error", () => {
            const err = player.error();
            options.onError(formatVideoError(err, source && source.type));
        });
    }
    if (options && typeof options.onReady === "function") {
        player.ready(() => options.onReady(player));
    }
    if (options && typeof options.onLoadedMetadata === "function") {
        player.on("loadedmetadata", () => {
            const width = player.videoWidth ? player.videoWidth() : 0;
            const height = player.videoHeight ? player.videoHeight() : 0;
            options.onLoadedMetadata({
                width,
                height,
                aspectRatio: width > 0 && height > 0 ? width / height : 0
            }, player);
        });
    }
    return player;
}

export function disposeVideoPlayer(player) {
    if (player && typeof player.dispose === "function") {
        player.dispose();
    }
}

function formatVideoError(err, type) {
    if (!err) {
        return "播放器初始化失败";
    }
    if (type === "application/x-mpegURL") {
        return "m3u8 播放失败，请确认切片文件可访问或稍后重试";
    }
    if (err.code === 4) {
        return "当前浏览器暂不支持该视频格式";
    }
    if (err.code === 2) {
        return "视频加载中断，请重试";
    }
    return err.message || "视频播放失败";
}
