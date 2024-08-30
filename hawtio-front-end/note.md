# ref
https://medium.com/@maayan_37411/how-to-create-a-react-multi-package-ui-library-2ba6ae0909b6

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


   pnpm add -D --filter console-ui-vite @testing-library/jest-dom vitest jsdom @testing-library/react

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
