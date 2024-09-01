/// <reference types="vite/client" />
/// <reference types="vitest" />
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import sass from 'sass';

// https://vitejs.dev/config/
const VITE_APP_CONSOLE_BASE_URL = process.env.VITE_APP_CONSOLE_BASE_URL;
export default defineConfig({
  // base: "/my-camel/admin/console",
  base: `${VITE_APP_CONSOLE_BASE_URL}/`,
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
      '/my-camel/admin/actuator/hawtio/auth/config': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          secure: false,
          // rewrite: path => path.replace('/console/auth/config', '/actuator/hawtio/auth/config')
      },
      '/my-camel/admin/actuator/hawtio/keycloak/enabled': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          secure: false,
          // rewrite: path => path.replace('/console/keycloak/enabled', '/actuator/hawtio/keycloak/enabled')
      },
      '/my-camel/admin/actuator/hawtio/proxy/enabled': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          secure: false,
          // rewrite: path => path.replace('/console/proxy/enabled', '/actuator/hawtio/proxy/enabled')
      },
      '/my-camel/admin/actuator/hawtio/plugin': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          secure: false,
          // rewrite: path => path.replace('/console/plugin', '/actuator/hawtio/plugin')
      },
      '/my-camel/admin/actuator/hawtio/user': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          secure: false,
          // rewrite: path => path.replace('/console/user', '/actuator/hawtio/user')
      },
      '/my-camel/admin/actuator/hawtio/jolokia': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          secure: false,
          // rewrite: path => path.replace('/console/jolokia', '/actuator/hawtio/jolokia')
      },
      '/my-camel/admin/actuator/hawtio/refresh': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          secure: false,
          // rewrite: path => path.replace('/console/refresh', '/actuator/hawtio/refresh')
      },
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
