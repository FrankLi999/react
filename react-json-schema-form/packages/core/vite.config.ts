/// <reference types="vite/client" />
/// <reference types="vitest" />
import { defineConfig } from 'vite'
import { extname, relative, resolve } from 'path'
import { fileURLToPath } from 'node:url'
import { glob } from 'glob'
import react from '@vitejs/plugin-react'
import dts from 'vite-plugin-dts'
import { libInjectCss } from 'vite-plugin-lib-inject-css'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    react(),
    libInjectCss(),
    dts({
      include: ['src'],
      insertTypesEntry: true,
      exclude: ['**/*.stories.ts', 'src/test', '**/*.test.tsx']
    })
  ],
  build: {
    copyPublicDir: false,
    lib: {
      entry: resolve(__dirname, 'src/index.ts'),
      formats: ['es']
    },
    rollupOptions: {
      external: ['react', 'react/jsx-runtime'],
      input: Object.fromEntries(
        // https://rollupjs.org/configuration-options/#input
        glob.sync(['src/components/**/index.{ts,tsx}', 'src/index.{ts,tsx}'],  {
          ignore: ["src/**/*.d.ts"],
        }).map(file => [
          // 1. The name of the entry point
          // src/nested/foo.js becomes nested/foo
          relative(
            'src',
            file.slice(0, file.length - extname(file).length)
          ),
          // 2. The absolute path to the entry file
          // src/nested/foo.ts becomes /project/src/nested/foo.ts
          fileURLToPath(new URL(file, import.meta.url))
        ])
      ),
      output: {
        assetFileNames: 'assets/[name][extname]',
        entryFileNames: '[name].js',
        globals: {
          react: 'React',
          'react-dom': 'React-dom',
          'react/jsx-runtime': 'react/jsx-runtime',
        },
      }
    }
  },
  // test: {
  //   globals: true,
  //   environment: 'jsdom',
  //   setupFiles: './src/test/setup.ts',
  //   // you might want to disable it, if you don't have tests that rely on CSS
  //   // since parsing CSS is slow
  //   css: true,
  //   coverage: {
  //     include: ['src/components'],
  //     exclude: ['**/*.stories.ts'],
  //   },
  // },
})