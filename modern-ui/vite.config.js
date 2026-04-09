import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig(({ command }) => ({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'),
      'vue': 'vue/dist/vue.esm-bundler.js'
    }
  },
  server: {
    port: 8002,
    proxy: {
      '/api': {
        target: 'http://localhost:8001',
        changeOrigin: true
      }
    }
  },
  base: command === 'build' ? '/modern-dist/' : '/',
  build: {
    outDir: resolve(__dirname, '../src/main/resources/static/modern-dist'),
    emptyOutDir: true,
    rollupOptions: {
      output: {
        entryFileNames: 'js/app.js',
        chunkFileNames: 'js/[name].js',
        assetFileNames: (assetInfo) => {
          if (assetInfo.name && assetInfo.name.endsWith('.css')) {
            return 'css/[name].[ext]'
          }
          return 'assets/[name].[ext]'
        },
        manualChunks: {
          'vendor': ['vue', 'vue-router', 'element-plus', '@element-plus/icons-vue'],
          'video': ['video.js']
        }
      }
    }
  }
}))
