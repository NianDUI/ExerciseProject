import {clearToken, getToken} from "../../common/auth.js";

export default {
    name: "LogLivePage",
    template: `
    <div class="card">
      <div class="toolbar">
        <el-button type="primary" @click="connect" :disabled="connected">连接</el-button>
        <el-button type="danger" @click="disconnect" :disabled="!connected">断开</el-button>
        <el-button @click="lines = []">清空</el-button>
        <el-button @click="resetToken">重置 token</el-button>
        <el-tag :type="connected ? 'success' : 'info'">{{ connected ? '已连接' : '未连接' }}</el-tag>
      </div>
      <div ref="box" style="max-height: 72vh; overflow: auto;">
        <div class="log-line" v-for="(line, index) in lines" :key="index">{{ line }}</div>
      </div>
    </div>
  `,
    data() {
        return {
            ws: null,
            connected: false,
            lines: []
        };
    },
    mounted() {
        this.connect();
    },
    beforeUnmount() {
        this.disconnect();
    },
    methods: {
        connect() {
            if (this.ws) {
                return;
            }
            const protocol = window.location.protocol === "https:" ? "wss://" : "ws://";
            this.ws = new window.WebSocket(`${protocol}${window.location.host}/api/log/ws`);
            this.ws.onopen = () => {
                this.connected = true;
                this.ws.send(getToken());
            };
            this.ws.onmessage = (evt) => {
                let txt = evt.data;
                try {
                    const obj = JSON.parse(evt.data);
                    txt = `${obj.date || ""} ${obj.level || ""} ${obj.thread || ""} ${obj.logger || ""} - ${obj.msg || ""} ${obj.errorDetail || ""}`.trim();
                } catch (e) {
                }
                this.lines.push(txt);
                if (this.lines.length > 200) {
                    this.lines.splice(0, this.lines.length - 200);
                }
                this.$nextTick(() => {
                    if (this.$refs.box) {
                        this.$refs.box.scrollTop = this.$refs.box.scrollHeight;
                    }
                });
            };
            this.ws.onclose = () => {
                this.connected = false;
                this.ws = null;
            };
            this.ws.onerror = () => {
                this.connected = false;
            };
        },
        disconnect() {
            if (this.ws) {
                this.ws.close();
                this.ws = null;
            }
            this.connected = false;
        },
        resetToken() {
            clearToken();
            window.ElementPlus.ElMessage.success("Token 已清理，下次连接会重新输入");
        }
    }
};
