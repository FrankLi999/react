# TODO:
  
  dts type definition
  scss

# ref 

https://victorlillo.dev/blog/react-typescript-vite-component-library
https://github.com/victor-lillo/react-vite-component-template

https://harbiola.hashnode.dev/setting-up-vite-with-react-sass-and-typescript

https://dev.to/lico/react-monorepo-setup-tutorial-with-pnpm-and-vite-react-project-ui-utils-5705

# Techs

  Feature	                     Technology used
  =======                   ==================
  Package manager	            PNPM
  Package bundler	            Vite
  Programming language    	  Typescript
  Basic linting	              ESLint
  Code formatting	            Prettier
  Pre-commit hook validator	  Husky
  Linting only staged files	  lint-staged
  Lint git commit subject	    commitlint

# steps

pnpm i json-schema-merge-allof jsonpointer lodash-es react-is --filter utils
pnpm i -D @types/json-schema @types/json-schema-merge-allof @types/lodash-es @types/node @types/react-is --filter utils

pnpm i -D @types/node vite-plugin-lib-inject-css vite-plugin-dts eslint-plugin-react-hooks prettier eslint-config-prettier stylelint stylelint-config-standard autoprefixer vitest jsdom @testing-library/react @vitest/ui @vitest/coverage-v8 --filter utils

under package folder,
## create project
   pnpm create vite

   utils, typescript + swc

## clean up
   deleting the Vite dev server-related files: App.css, index.css, App.tsx, main.tsx, and index.html.
   delete the dev and preview scripts from the package.json

## vite config in library mode
 
  npm i -D @types/node --filter utils


    ```
    /// <reference types="vite/client" />
    import { resolve } from 'node:path'
    import { defineConfig } from 'vite'
    import react from '@vitejs/plugin-react'

    export default defineConfig({
      plugins: [react()],
      build: {
        lib: {
          entry: resolve(__dirname, 'src/main.ts'),
          formats: ['es'],
        },
        rollupOptions: {
          external: ['react', 'react-dom', 'react/jsx-runtime'],
          output: {
            globals: {
              react: 'React',
              'react-dom': 'React-dom',
              'react/jsx-runtime': 'react/jsx-runtime',
            },
          },
        },
      },
    })
    ```



## css style

   npm i -D vite-plugin-lib-inject-css --filter utils

   vite config:
      plugins: [react(), libInjectCss()],
   
## Building library types
   npm i -D vite-plugin-dts

   vite config
   ```
    plugins: [
      ...
      dts({
        include: ['src'],
        insertTypesEntry: true,
        exclude: ['**/*.stories.ts', 'src/test', '**/*.test.tsx']
      })
      ...
    ],

   ``` 
## ESLint

   eslint-plugin-react-hooks

   eslint.config.js
   ```
    import js from '@eslint/js'
    import globals from 'globals'
    import reactHooks from 'eslint-plugin-react-hooks'
    import reactRefresh from 'eslint-plugin-react-refresh'
    import tseslint from 'typescript-eslint'

    export default tseslint.config({
      extends: [js.configs.recommended, ...tseslint.configs.recommended],
      files: ['**/*.{ts,tsx}'],
      ignores: ['dist'],
      languageOptions: {
        ecmaVersion: 2020,
        globals: globals.browser,
      },
      ignorePatterns: ['dist', 'eslint.config.js', 'postcss.config.cjs'],
      plugins: {
        'react-hooks': reactHooks,
        'react-refresh': reactRefresh,
      },
      rules: {
        ...reactHooks.configs.recommended.rules,
        'react-refresh/only-export-components': [
          'warn',
          { allowConstantExport: true },
        ],
      },
    })
   ```

## Prettier
 pnpm i -D prettier eslint-config-prettier --filter utils

 .prettierrc
 .prettierignore
 .eslintrc 
 package.json
    ```
    {
      // ...
      "scripts": {
        // ...
        "format": "prettier --check .",
        "format:fix": "prettier --write ."
      }
    }
    ```
## Stylelint
  pnpm i -D stylelint stylelint-config-standard --filter utils

  .stylelintrc.mjs
  .stylelintignore 

  package.json
  ```
    "stylelint": "stylelint **/*.css",
    "stylelint:fix": "stylelint **/*.css --fix"
  ```
## Storybook
   npx storybook@latest init

   add 'plugin:storybook/recommended' to eslint config

   update vite config, exclude stories from dts.
   package.json

## PostCSS
   
   pnpm i -D autoprefixer --filter utils
   
   postcss.config.cjs
   

##  Testing with Vitest

  pnpm i -D vitest jsdom @testing-library/react --filter utils

  package.json
  ```
    "test": "vitest"
  ```

  src/test/setup.ts
    ```
    import '@testing-library/jest-dom'
    import { afterEach } from 'vitest'
    import { cleanup } from '@testing-library/react'

    // hooks are reset before each suite
    afterEach(() => {
      cleanup()
    })
    ```
## Vitest UI and coverage    
  pnpm i -D @vitest/ui @vitest/coverage-v8 --filter

  package.json
    "test": "vitest --ui",
    "coverage": "vitest run --coverage"

## SASS
according to https://vitejs.dev/guide/features,  Because Vite targets modern browsers only, it is recommended to use native CSS variables with PostCSS plugins that implement CSSWG drafts (e.g. postcss-nesting) and author plain, future-standards-compliant CSS. That said, Vite does provide built-in support for .scss, .sass, .less, .styl and .stylus files. There is no need to install Vite-specific plugins for them, but the corresponding pre-processor itself must be installed:


  for .scss and .sass
  ```
  pnpm add -D sass-embedded --force # or sass
  ```

or 
https://victorlillo.dev/blog/react-typescript-vite-component-library
pnpm i -D sass sass-loader --filter utils

vite config
```
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import sass from 'sass'


export default defineConfig({
  plugins: [react()],
  css: {
    preprocessorOptions: {
      scss: {
        implementation: sass,
      },
    },
  },
});


```
# React + TypeScript + Vite

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react/README.md) uses [Babel](https://babeljs.io/) for Fast Refresh
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react-swc) uses [SWC](https://swc.rs/) for Fast Refresh

## Expanding the ESLint configuration

If you are developing a production application, we recommend updating the configuration to enable type aware lint rules:

- Configure the top-level `parserOptions` property like this:

```js
export default tseslint.config({
  languageOptions: {
    // other options...
    parserOptions: {
      project: ['./tsconfig.node.json', './tsconfig.app.json'],
      tsconfigRootDir: import.meta.dirname,
    },
  },
})
```

- Replace `tseslint.configs.recommended` to `tseslint.configs.recommendedTypeChecked` or `tseslint.configs.strictTypeChecked`
- Optionally add `...tseslint.configs.stylisticTypeChecked`
- Install [eslint-plugin-react](https://github.com/jsx-eslint/eslint-plugin-react) and update the config:

```js
// eslint.config.js
import react from 'eslint-plugin-react'

export default tseslint.config({
  // Set the react version
  settings: { react: { version: '18.3' } },
  plugins: {
    // Add the react plugin
    react,
  },
  rules: {
    // other rules...
    // Enable its recommended rules
    ...react.configs.recommended.rules,
    ...react.configs['jsx-runtime'].rules,
  },
})
```
