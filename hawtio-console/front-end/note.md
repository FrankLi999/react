# ref
https://medium.com/@nedopaka/setup-a-react-vite-project-with-prettier-vitest-2024-12e291669b4b
https://dev.to/marcosdiasdev/adding-eslint-and-prettier-to-a-vitejs-react-project-2kkj
https://medium.com/@karpov-kir/simple-react-app-with-vite-typescript-linting-formatting-f0a3ee41dd2c

https://harbiola.hashnode.dev/setting-up-vite-with-react-sass-and-typescript

https://victorlillo.dev/blog/react-typescript-vite-component-library
https://github.com/victor-lillo/react-vite-component-template?tab=readme-ov-file
# steps
## root directory
cd mono-vite-repo
pnpm init

create pnpm-workspace.yaml
  ```
  packages:
  - 'packages/*'
  - 'website'
  ```
## create website project
pnpm create vite  
    project name: website
    framework: react
    variant: Typescript + SWC

pnpm install    

add the following line to package.json from the root folder

    ```
    //...
    "scripts": {
    "dev": "pnpm --filter website dev"
    },
    //...
    ```
pnpm run dev    
[run website](http://localhost:5173/)


## packages/utils
    create utils directory under packages
    > mkdir -p packages/utils

    package.json: The package has been renamed with @mono/utils and set the main file path.
        ```
        {
            "name": "@mono/utils",
            "version": "0.0.1",
            "main": "src/index.ts",
            "scripts": {
                "test": "vitest run"
            }
        }
        ```

    setup typescript environment:
        from root directory, run
        ```
        pnpm install -D typescript --filter utils
        ```
        from package/utils directory:
        ```
        pnpm exec tsc --init
        ```
        from root directory, run
        ```
        pnpm install -D vitest --filter utils
        ```    

        create src directory and add calc.ts, calc.test.ts and index.ts under src

## packages/ui

   cd packages
   npm create vite@latest ui -- --template react-ts

   or 
   pnpm create vite

         project name: ui
        framework: react
        variant: Typescript
        
   delete unnecessary files: all files under src, public dir, index.html      

   from root, run pnpm install
   create src/index.ts, src/components/Button.tsx and , src/components/Button.test.tsx


   pnpm add -D --filter ui @testing-library/jest-dom vitest jsdom @testing-library/react

   packages/ui/vitest.config.ts:
      ```
        import { defineConfig } from 'vitest/config';
        import react from '@vitejs/plugin-react-swc';

        export default defineConfig(({ mode }) => ({
        plugins: [react()],
        resolve: {
            conditions: mode === 'test' ? ['browser'] : [],
        },
        test: {
            environment: 'jsdom',
            setupFiles: ['./vitest-setup.js'],
        },
        }));
      ```

    packages/ui/vitest-setup.js
          ```
          import '@testing-library/jest-dom/vitest';
          ```

    [packages/ui/tsconfig.app.json]

        ```
        {
        //...
            "types": ["@testing-library/jest-dom"],
        //...
        }
        ```

    [src/packages/ui/package.json]

        {
            "name": "@mono/ui",
            "main": "src/index.ts",
            //...
            "scripts": {
                "test": "vitest run"
            },
            //...
        }

    pnpm test:all

## Using packages on the website

## hawtio console-ui

pnpm create react-app console-ui --template typescript


pnpm i @hawtio/react jolokia.js@2.0.3 keycloak-js@23.0.7 --filter console-ui



 


pnpm i @fortawesome/fontawesome-free @patternfly/react-core @patternfly/react-icons @patternfly/react-table ajv ajv-formats ajv-i18n axios http-proxy-middleware i18next i18next-browser-languagedetector i18next-http-backend jsonpointer lodash-es markdown-to-jsx nanoid react-cookie  react-i18next react-icons react-is react-router-dom@6.26.1 --filter console-ui


pnpm i @react-jsf/utils @react-jsf/ajv @react-jsf/core @react-jsf/patternfly --filter console-ui

pnpm i -D @types/json-schema @types/json-schema-merge-allof @types/lodash-es @types/react-is --filter console-ui


## hawtio console-ui-vite

   npm create vite@latest console-ui-vite -- --template react-ts

   or 
   pnpm create vite

         project name: console-ui-vite
        framework: react
        variant: Typescript


pnpm i @hawtio/react jolokia.js@2.0.3 keycloak-js@23.0.7 --filter console-ui-vite



 


pnpm i @fortawesome/fontawesome-free @patternfly/react-core @patternfly/react-icons @patternfly/react-table ajv ajv-formats ajv-i18n axios http-proxy-middleware i18next i18next-browser-languagedetector i18next-http-backend jsonpointer lodash-es markdown-to-jsx nanoid react-cookie  react-i18next react-icons react-is react-router-dom@6.26.1 --filter console-ui-vite


pnpm i @react-jsf/utils @react-jsf/ajv @react-jsf/core @react-jsf/patternfly --filter console-ui-vite

pnpm i -D @types/json-schema @types/json-schema-merge-allof @types/lodash-es @types/react-is --filter console-ui-vite


### out of the box eslint
eslint
eslint-plugin-react-hooks - enforces Rules of Hooks
eslint-plugin-react-refresh - validates that your components can safely be updated with fast refresh

eslint.config.js

### vitest
pnpm add -D --filter console-ui-vite @testing-library/jest-dom vitest jsdom @testing-library/react
         
packages/ui/vitte.config.ts:
      ```
        import { defineConfig } from 'vite';
        import react from '@vitejs/plugin-react';

        // https://vitejs.dev/config/
        export default defineConfig({
        plugins: [react()],
        test: {
            globals: true,
            environment: 'jsdom',
            setupFiles: ['src/setupTests.ts'],
        },
        });
      ```

    src/setupTests.js
          ```
          import '@testing-library/jest-dom/vitest';
          ```

    [packages/ui/tsconfig.app.json]

        ```
        {
        //...
            "types": ["@testing-library/jest-dom"],
        //...
        }
        ```

     [src/packages/ui/package.json]

        {
            "name": "@mono/ui",
            "main": "src/index.ts",
            //...
            "scripts": {
                "test": "vitest run"
            },
            //...
        }

      pnpm test:all   

#### vitest for eslint

### Sass and sass loader      
pnpm i -D sass sass-loader --filter console-ui-vite

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

### prettier

pnpm install prettier eslint-config-prettier eslint-plugin-prettier eslint-plugin-simple-import-sort --save-dev --filter console-ui-vite

    eslint-config-prettier eslint-plugin-prettier - it’s used for integration of Prettier with ESLint
    plugin-simple-import-sort - sorts and formates imports

eslint.config.js
```
import js from '@eslint/js'
import globals from 'globals'
import reactHooks from 'eslint-plugin-react-hooks'
import reactRefresh from 'eslint-plugin-react-refresh'
import prettier from 'eslint-plugin-prettier'
import tseslint from 'typescript-eslint'

export default tseslint.config({
  extends: [js.configs.recommended, ...tseslint.configs.recommended, ...prettier.configs.recommended],
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
    "prettier": prettier
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


### style lint
pnpm install stylelint stylelint-prettier stylelint-config-standard --save-dev --filter console-ui-vite

### a11y

eslint-plugin-jsx-a11y