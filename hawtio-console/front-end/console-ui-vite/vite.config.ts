import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
const VITE_APP_CONSOLE_BASE_URL = process.env.VITE_APP_CONSOLE_BASE_URL;
export default defineConfig({
  // base: "/my-camel/admin/console",
  base: `${VITE_APP_CONSOLE_BASE_URL}/`,
  plugins: [react()],
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
  }
})
