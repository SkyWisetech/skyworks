import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      resolvers: [ElementPlusResolver()]
    }),
    Components({
      resolvers: [
        // 配置elementplus采用样式配色系统
        ElementPlusResolver({ importStyle: 'sass' })
      ]
    })
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  // 设置默认主题色
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `
        @forward 'element-plus/theme-chalk/src/common/var.scss' with (
  $colors: (
    'primary': (
      'base': #7554e6,
    ),
  ),
);
        
        `
      }
    }
  },
  transpileDependencies: true,
  // devServer: {
  //   host: '127.0.0.1', // 此前端项目的IP地址
  //   port: 5173, // 此前端项目的端口号
  //   open: true, //表示在启动开发服务器时，会自动打开浏览器并访问指定的地址
  //   proxy: {
  //     '/api': {
  //       target: 'http://192.168.1.3:8080', //接口域名
  //       changeOrigin: true, //是否跨域
  //       ws: true, //是否代理 websockets
  //       secure: false, //是否https接口
  //       pathRewrite: {
  //         '^/api': '' //假如我们的地址是 /api/member/getToken 会转化为 /member/getToken
  //       }
  //     }
  //   }
  // }
  build: {
    sourcemap: false,
    minify: 'terser',
    terserOptions: {
      compress: {
        drop_console: true,
        drop_debugger: true
      }
    }
  }
})
