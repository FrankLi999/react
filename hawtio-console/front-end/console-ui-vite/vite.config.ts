import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  base: "/my-camel/admin/console/",
  // base: `${process.env.VITE_APP_BASE_URL}`,
  plugins: [react()],
  server: {
    port:3000,
    proxy: {
      '/my-camel/admin/console/auth/config': {
          target: 'http://localhost:8080/my-camel/admin/actuator/hawtio/auth/config',
          changeOrigin: true,
      },
      '/my-camel/admin/console/keycloak/enabled': {
          target: 'http://localhost:8080/my-camel/admin/actuator/hawtio/keycloak/enabled',
          changeOrigin: true,
      },
      '/my-camel/admin/console/proxy/enabled': {
          target: 'http://localhost:8080/my-camel/admin/actuator/hawtio/proxy/enabled',
          changeOrigin: true,
      },
      '/my-camel/admin/console/plugin': {
          target: 'http://localhost:8080/my-camel/admin/actuator/hawtio/plugin',
          changeOrigin: true,
      },
      '/my-camel/admin/console/user': {
          target: 'http://localhost:8080/my-camel/admin/actuator/hawtio/user',
          changeOrigin: true,
      },
      '/my-camel/admin/console/jolokia': {
          target: 'http://localhost:8080/my-camel/admin/actuator/hawtio/jolokia',
          changeOrigin: true,
      },
      '/my-camel/admin/console/refresh': {
          target: 'http://localhost:8080/my-camel/admin/actuator/hawtio/refresh',
          changeOrigin: true,
      },
      '/my-camel/admin/api': {
          target: 'http://localhost:8080/my-camel/admin/api',
          changeOrigin: true
      },
      '/my-camel/admin/web/i18next': {
        target: 'http://localhost:8080/my-camel/admin/web/i18next',
        changeOrigin: true
      }
    }
  }
})
