// vite.config.js
import { fileURLToPath, URL } from "node:url";
import { defineConfig } from "file:///F:/%E5%A4%A9%E4%BA%A6%E6%99%BA%E4%BA%91/projects/%E5%A4%A9%E5%B7%A5%E5%BC%80%E7%89%A9/skyworks_web_front/node_modules/.pnpm/vite@5.3.2_sass@1.77.6_terser@5.31.3/node_modules/vite/dist/node/index.js";
import vue from "file:///F:/%E5%A4%A9%E4%BA%A6%E6%99%BA%E4%BA%91/projects/%E5%A4%A9%E5%B7%A5%E5%BC%80%E7%89%A9/skyworks_web_front/node_modules/.pnpm/@vitejs+plugin-vue@5.0.5_vite@5.3.2_sass@1.77.6_terser@5.31.3__vue@3.4.31/node_modules/@vitejs/plugin-vue/dist/index.mjs";
import AutoImport from "file:///F:/%E5%A4%A9%E4%BA%A6%E6%99%BA%E4%BA%91/projects/%E5%A4%A9%E5%B7%A5%E5%BC%80%E7%89%A9/skyworks_web_front/node_modules/.pnpm/unplugin-auto-import@0.17.6_@vueuse+core@9.13.0_vue@3.4.31__rollup@4.18.0/node_modules/unplugin-auto-import/dist/vite.js";
import Components from "file:///F:/%E5%A4%A9%E4%BA%A6%E6%99%BA%E4%BA%91/projects/%E5%A4%A9%E5%B7%A5%E5%BC%80%E7%89%A9/skyworks_web_front/node_modules/.pnpm/unplugin-vue-components@0.27.2_@babel+parser@7.24.7_rollup@4.18.0_vue@3.4.31/node_modules/unplugin-vue-components/dist/vite.js";
import { ElementPlusResolver } from "file:///F:/%E5%A4%A9%E4%BA%A6%E6%99%BA%E4%BA%91/projects/%E5%A4%A9%E5%B7%A5%E5%BC%80%E7%89%A9/skyworks_web_front/node_modules/.pnpm/unplugin-vue-components@0.27.2_@babel+parser@7.24.7_rollup@4.18.0_vue@3.4.31/node_modules/unplugin-vue-components/dist/resolvers.js";
var __vite_injected_original_import_meta_url = "file:///F:/%E5%A4%A9%E4%BA%A6%E6%99%BA%E4%BA%91/projects/%E5%A4%A9%E5%B7%A5%E5%BC%80%E7%89%A9/skyworks_web_front/vite.config.js";
var vite_config_default = defineConfig({
  plugins: [
    vue(),
    AutoImport({
      resolvers: [ElementPlusResolver()]
    }),
    Components({
      resolvers: [
        // 配置elementplus采用样式配色系统
        ElementPlusResolver({ importStyle: "sass" })
      ]
    })
  ],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", __vite_injected_original_import_meta_url))
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
  devServer: {
    host: "127.0.0.1",
    // 此前端项目的IP地址
    port: 5174,
    // 此前端项目的端口号
    open: true,
    //表示在启动开发服务器时，会自动打开浏览器并访问指定的地址
    proxy: {
      "/api": {
        target: "http://192.168.1.3:8080",
        //接口域名
        changeOrigin: true,
        //是否跨域
        ws: true,
        //是否代理 websockets
        secure: false,
        //是否https接口
        pathRewrite: {
          "^/api": ""
          //假如我们的地址是 /api/member/getToken 会转化为 /member/getToken
        }
      }
    }
  },
  dev: {
    sourcemap: false,
    minify: "terser",
    terserOptions: {
      compress: {
        drop_console: true,
        drop_debugger: true
      }
    }
  }
});
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcuanMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJGOlxcXFxcdTU5MjlcdTRFQTZcdTY2N0FcdTRFOTFcXFxccHJvamVjdHNcXFxcXHU1OTI5XHU1REU1XHU1RjAwXHU3MjY5XFxcXHNreXdvcmtzX3dlYl9mcm9udFwiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiRjpcXFxcXHU1OTI5XHU0RUE2XHU2NjdBXHU0RTkxXFxcXHByb2plY3RzXFxcXFx1NTkyOVx1NURFNVx1NUYwMFx1NzI2OVxcXFxza3l3b3Jrc193ZWJfZnJvbnRcXFxcdml0ZS5jb25maWcuanNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsID0gXCJmaWxlOi8vL0Y6LyVFNSVBNCVBOSVFNCVCQSVBNiVFNiU5OSVCQSVFNCVCQSU5MS9wcm9qZWN0cy8lRTUlQTQlQTklRTUlQjclQTUlRTUlQkMlODAlRTclODklQTkvc2t5d29ya3Nfd2ViX2Zyb250L3ZpdGUuY29uZmlnLmpzXCI7aW1wb3J0IHsgZmlsZVVSTFRvUGF0aCwgVVJMIH0gZnJvbSAnbm9kZTp1cmwnXHJcblxyXG5pbXBvcnQgeyBkZWZpbmVDb25maWcgfSBmcm9tICd2aXRlJ1xyXG5pbXBvcnQgdnVlIGZyb20gJ0B2aXRlanMvcGx1Z2luLXZ1ZSdcclxuXHJcbmltcG9ydCBBdXRvSW1wb3J0IGZyb20gJ3VucGx1Z2luLWF1dG8taW1wb3J0L3ZpdGUnXHJcbmltcG9ydCBDb21wb25lbnRzIGZyb20gJ3VucGx1Z2luLXZ1ZS1jb21wb25lbnRzL3ZpdGUnXHJcbmltcG9ydCB7IEVsZW1lbnRQbHVzUmVzb2x2ZXIgfSBmcm9tICd1bnBsdWdpbi12dWUtY29tcG9uZW50cy9yZXNvbHZlcnMnXHJcbmV4cG9ydCBkZWZhdWx0IGRlZmluZUNvbmZpZyh7XHJcbiAgcGx1Z2luczogW1xyXG4gICAgdnVlKCksXHJcbiAgICBBdXRvSW1wb3J0KHtcclxuICAgICAgcmVzb2x2ZXJzOiBbRWxlbWVudFBsdXNSZXNvbHZlcigpXVxyXG4gICAgfSksXHJcbiAgICBDb21wb25lbnRzKHtcclxuICAgICAgcmVzb2x2ZXJzOiBbXHJcbiAgICAgICAgLy8gXHU5MTREXHU3RjZFZWxlbWVudHBsdXNcdTkxQzdcdTc1MjhcdTY4MzdcdTVGMEZcdTkxNERcdTgyNzJcdTdDRkJcdTdFREZcclxuICAgICAgICBFbGVtZW50UGx1c1Jlc29sdmVyKHsgaW1wb3J0U3R5bGU6ICdzYXNzJyB9KVxyXG4gICAgICBdXHJcbiAgICB9KVxyXG4gIF0sXHJcbiAgcmVzb2x2ZToge1xyXG4gICAgYWxpYXM6IHtcclxuICAgICAgJ0AnOiBmaWxlVVJMVG9QYXRoKG5ldyBVUkwoJy4vc3JjJywgaW1wb3J0Lm1ldGEudXJsKSlcclxuICAgIH1cclxuICB9LFxyXG4gIC8vIFx1OEJCRVx1N0Y2RVx1OUVEOFx1OEJBNFx1NEUzQlx1OTg5OFx1ODI3MlxyXG4gIGNzczoge1xyXG4gICAgcHJlcHJvY2Vzc29yT3B0aW9uczoge1xyXG4gICAgICBzY3NzOiB7XHJcbiAgICAgICAgYWRkaXRpb25hbERhdGE6IGBcclxuICAgICAgICBAZm9yd2FyZCAnZWxlbWVudC1wbHVzL3RoZW1lLWNoYWxrL3NyYy9jb21tb24vdmFyLnNjc3MnIHdpdGggKFxyXG4gICRjb2xvcnM6IChcclxuICAgICdwcmltYXJ5JzogKFxyXG4gICAgICAnYmFzZSc6ICM3NTU0ZTYsXHJcbiAgICApLFxyXG4gICksXHJcbik7XHJcbiAgICAgICAgXHJcbiAgICAgICAgYFxyXG4gICAgICB9XHJcbiAgICB9XHJcbiAgfSxcclxuICB0cmFuc3BpbGVEZXBlbmRlbmNpZXM6IHRydWUsXHJcbiAgZGV2U2VydmVyOiB7XHJcbiAgICBob3N0OiAnMTI3LjAuMC4xJywgLy8gXHU2QjY0XHU1MjREXHU3QUVGXHU5ODc5XHU3NkVFXHU3Njg0SVBcdTU3MzBcdTU3NDBcclxuICAgIHBvcnQ6IDUxNzQsIC8vIFx1NkI2NFx1NTI0RFx1N0FFRlx1OTg3OVx1NzZFRVx1NzY4NFx1N0FFRlx1NTNFM1x1NTNGN1xyXG4gICAgb3BlbjogdHJ1ZSwgLy9cdTg4NjhcdTc5M0FcdTU3MjhcdTU0MkZcdTUyQThcdTVGMDBcdTUzRDFcdTY3MERcdTUyQTFcdTU2NjhcdTY1RjZcdUZGMENcdTRGMUFcdTgxRUFcdTUyQThcdTYyNTNcdTVGMDBcdTZENEZcdTg5QzhcdTU2NjhcdTVFNzZcdThCQkZcdTk1RUVcdTYzMDdcdTVCOUFcdTc2ODRcdTU3MzBcdTU3NDBcclxuICAgIHByb3h5OiB7XHJcbiAgICAgICcvYXBpJzoge1xyXG4gICAgICAgIHRhcmdldDogJ2h0dHA6Ly8xOTIuMTY4LjEuMzo4MDgwJywgLy9cdTYzQTVcdTUzRTNcdTU3REZcdTU0MERcclxuICAgICAgICBjaGFuZ2VPcmlnaW46IHRydWUsIC8vXHU2NjJGXHU1NDI2XHU4REU4XHU1N0RGXHJcbiAgICAgICAgd3M6IHRydWUsIC8vXHU2NjJGXHU1NDI2XHU0RUUzXHU3NDA2IHdlYnNvY2tldHNcclxuICAgICAgICBzZWN1cmU6IGZhbHNlLCAvL1x1NjYyRlx1NTQyNmh0dHBzXHU2M0E1XHU1M0UzXHJcbiAgICAgICAgcGF0aFJld3JpdGU6IHtcclxuICAgICAgICAgICdeL2FwaSc6ICcnIC8vXHU1MDQ3XHU1OTgyXHU2MjExXHU0RUVDXHU3Njg0XHU1NzMwXHU1NzQwXHU2NjJGIC9hcGkvbWVtYmVyL2dldFRva2VuIFx1NEYxQVx1OEY2Q1x1NTMxNlx1NEUzQSAvbWVtYmVyL2dldFRva2VuXHJcbiAgICAgICAgfVxyXG4gICAgICB9XHJcbiAgICB9XHJcbiAgfSxcclxuICBkZXY6IHtcclxuICAgIHNvdXJjZW1hcDogZmFsc2UsXHJcbiAgICBtaW5pZnk6ICd0ZXJzZXInLFxyXG4gICAgdGVyc2VyT3B0aW9uczoge1xyXG4gICAgICBjb21wcmVzczoge1xyXG4gICAgICAgIGRyb3BfY29uc29sZTogdHJ1ZSxcclxuICAgICAgICBkcm9wX2RlYnVnZ2VyOiB0cnVlXHJcbiAgICAgIH1cclxuICAgIH1cclxuICB9XHJcbn0pXHJcbiJdLAogICJtYXBwaW5ncyI6ICI7QUFBb1gsU0FBUyxlQUFlLFdBQVc7QUFFdlosU0FBUyxvQkFBb0I7QUFDN0IsT0FBTyxTQUFTO0FBRWhCLE9BQU8sZ0JBQWdCO0FBQ3ZCLE9BQU8sZ0JBQWdCO0FBQ3ZCLFNBQVMsMkJBQTJCO0FBUDZKLElBQU0sMkNBQTJDO0FBUWxQLElBQU8sc0JBQVEsYUFBYTtBQUFBLEVBQzFCLFNBQVM7QUFBQSxJQUNQLElBQUk7QUFBQSxJQUNKLFdBQVc7QUFBQSxNQUNULFdBQVcsQ0FBQyxvQkFBb0IsQ0FBQztBQUFBLElBQ25DLENBQUM7QUFBQSxJQUNELFdBQVc7QUFBQSxNQUNULFdBQVc7QUFBQTtBQUFBLFFBRVQsb0JBQW9CLEVBQUUsYUFBYSxPQUFPLENBQUM7QUFBQSxNQUM3QztBQUFBLElBQ0YsQ0FBQztBQUFBLEVBQ0g7QUFBQSxFQUNBLFNBQVM7QUFBQSxJQUNQLE9BQU87QUFBQSxNQUNMLEtBQUssY0FBYyxJQUFJLElBQUksU0FBUyx3Q0FBZSxDQUFDO0FBQUEsSUFDdEQ7QUFBQSxFQUNGO0FBQUE7QUFBQSxFQUVBLEtBQUs7QUFBQSxJQUNILHFCQUFxQjtBQUFBLE1BQ25CLE1BQU07QUFBQSxRQUNKLGdCQUFnQjtBQUFBO0FBQUE7QUFBQTtBQUFBO0FBQUE7QUFBQTtBQUFBO0FBQUE7QUFBQTtBQUFBLE1BVWxCO0FBQUEsSUFDRjtBQUFBLEVBQ0Y7QUFBQSxFQUNBLHVCQUF1QjtBQUFBLEVBQ3ZCLFdBQVc7QUFBQSxJQUNULE1BQU07QUFBQTtBQUFBLElBQ04sTUFBTTtBQUFBO0FBQUEsSUFDTixNQUFNO0FBQUE7QUFBQSxJQUNOLE9BQU87QUFBQSxNQUNMLFFBQVE7QUFBQSxRQUNOLFFBQVE7QUFBQTtBQUFBLFFBQ1IsY0FBYztBQUFBO0FBQUEsUUFDZCxJQUFJO0FBQUE7QUFBQSxRQUNKLFFBQVE7QUFBQTtBQUFBLFFBQ1IsYUFBYTtBQUFBLFVBQ1gsU0FBUztBQUFBO0FBQUEsUUFDWDtBQUFBLE1BQ0Y7QUFBQSxJQUNGO0FBQUEsRUFDRjtBQUFBLEVBQ0EsS0FBSztBQUFBLElBQ0gsV0FBVztBQUFBLElBQ1gsUUFBUTtBQUFBLElBQ1IsZUFBZTtBQUFBLE1BQ2IsVUFBVTtBQUFBLFFBQ1IsY0FBYztBQUFBLFFBQ2QsZUFBZTtBQUFBLE1BQ2pCO0FBQUEsSUFDRjtBQUFBLEVBQ0Y7QUFDRixDQUFDOyIsCiAgIm5hbWVzIjogW10KfQo=
