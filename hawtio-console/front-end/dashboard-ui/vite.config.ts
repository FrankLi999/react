/// <reference types="vite/client" />
/// <reference types="vitest" />
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import sass from 'sass';

// https://vitejs.dev/config/
const VITE_APP_DASHBOARD_BASE_URL = process.env.VITE_APP_DASHBOARD_BASE_URL;
export default defineConfig({
  // base: "/my-camel/admin/console",
  base: `${VITE_APP_DASHBOARD_BASE_URL}/`,
  plugins: [react()],
  css: {
    preprocessorOptions: {
      scss: {
        implementation: sass,
      },
    },
  },
  build: {
    rollupOptions: {
      output: {
        assetFileNames: (assetInfo: any) => {
          var info = assetInfo.name.split(".");
          var extType = info[info.length - 1];
          if (/png|jpe?g|svg|gif|tiff|bmp|ico/i.test(extType)) {
            extType = "img";
          } else if (/woff|woff2/.test(extType)) {
            extType = "css";
          }
          return `static/${extType}/[name]-[hash][extname]`;
        },
        chunkFileNames: "static/js/[name]-[hash].js",
        entryFileNames: "static/js/[name]-[hash].js",
      },
    }
  },
  server: {
    port:3000,
    proxy: {
      '/my-camel/admin/api': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          secure: false
      },
      '/my-camel/admin/web/i18next': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      }
    }
  },
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: ['src/setupTests.ts'],
  },
})
