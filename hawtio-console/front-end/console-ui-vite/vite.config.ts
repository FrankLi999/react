import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  base: "/my-camel/admin/console",
  // base: process.env.VITE_APP_BASE_URL,
  plugins: [react()],
  server: {
    port:3000,
    proxy: {
      '/my-camel/admin/console/auth/config': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          secure: false,
          rewrite: path => path.replace('/console/auth/config', '/actuator/hawtio/auth/config')
      },
      '/my-camel/admin/console/keycloak/enabled': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          secure: false,
          rewrite: path => path.replace('/console/keycloak/enabled', '/actuator/hawtio/keycloak/enabled')
      },
      '/my-camel/admin/console/proxy/enabled': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          secure: false,
          rewrite: path => path.replace('/console/proxy/enabled', '/actuator/hawtio/proxy/enabled')
      },
      '/my-camel/admin/console/plugin': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          secure: false,
          rewrite: path => path.replace('/console/plugin', '/actuator/hawtio/plugin')
      },
      '/my-camel/admin/console/user': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          secure: false,
          rewrite: path => path.replace('/console/user', '/actuator/hawtio/user')
      },
      '/my-camel/admin/console/jolokia': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          secure: false,
          rewrite: path => path.replace('/console/jolokia', '/actuator/hawtio/jolokia')
      },
      '/my-camel/admin/console/refresh': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          secure: false,
          rewrite: path => path.replace('/console/refresh', '/actuator/hawtio/refresh')
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
