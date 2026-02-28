import {clearToken, setTokenByPrompt} from "../../../modern/js/common/auth.js";
import {clearMobileCache, getReaderPreference, saveReaderPreference} from "../common/storage.js";

export default {
    name: "MobileProfilePage",
    template: `
    <div class="mobile-page">
      <section class="mobile-section">
        <div class="catalog-head">
          <div>
            <p class="hero-kicker">我的</p>
            <h2 class="catalog-title">设置与偏好</h2>
          </div>
        </div>

        <div class="setting-card">
          <div>
            <div class="setting-title">接口 Token</div>
            <div class="setting-desc">手机端继续沿用现有 token 机制。</div>
          </div>
          <div class="row-gap">
            <el-button type="primary" round @click="updateToken">设置 Token</el-button>
            <el-button round @click="removeToken">清除 Token</el-button>
          </div>
        </div>

        <div class="setting-card">
          <div>
            <div class="setting-title">阅读主题</div>
            <div class="setting-desc">当前 {{ themeLabel }}</div>
          </div>
          <el-select v-model="preference.theme" style="width: 140px;" @change="saveTheme">
            <el-option label="米白" value="paper" />
            <el-option label="浅绿" value="mint" />
            <el-option label="深色" value="night" />
          </el-select>
        </div>

        <div class="setting-card">
          <div>
            <div class="setting-title">缓存</div>
            <div class="setting-desc">清除本地阅读进度与续读记录。</div>
          </div>
          <el-button round @click="clearCache">清除缓存</el-button>
        </div>
      </section>
    </div>
  `,
    data() {
        return {
            preference: getReaderPreference()
        };
    },
    computed: {
        themeLabel() {
            return {
                paper: "米白",
                mint: "浅绿",
                night: "深色"
            }[this.preference.theme] || "米白";
        }
    },
    methods: {
        updateToken() {
            setTokenByPrompt();
            window.ElementPlus.ElMessage.success("Token 已更新");
        },
        removeToken() {
            clearToken();
            window.ElementPlus.ElMessage.success("Token 已清除");
        },
        saveTheme() {
            saveReaderPreference({ theme: this.preference.theme });
            window.ElementPlus.ElMessage.success("阅读主题已保存");
        },
        clearCache() {
            clearMobileCache();
            window.ElementPlus.ElMessage.success("本地缓存已清除");
        }
    }
};
