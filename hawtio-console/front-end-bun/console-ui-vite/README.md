## dev and build 
set VITE_APP_CONSOLE_BASE_URL=/my-camel/admin/actuator/hawtio
export VITE_APP_CONSOLE_BASE_URL=/my-camel/admin/actuator/hawtio
bun run dev
bun run build

http://localhost:8080/my-camel/admin/actuator/hawtio/
http://localhost:3000/my-camel/admin/actuator/hawtio/
## misc
pnpm i @react-jsf/utils @react-jsf/ajv @react-jsf/core @react-jsf/bootstrap5 @react-jsf/patternfly --filter sample

pnpm i @fluentui/react @patternfly/react-core @patternfly/react-icons bootstrap react-icons ajv ajv-formats ajv-i18n dayjs lodash-es react-bootstrap react-is react-portal --filter sample

pnpm i -D @types/node @types/lodash-es --filter sample

pnpm i -D vite-plugin-lib-inject-css glob vite-plugin-dts eslint-plugin-react-hooks prettier eslint-config-prettier stylelint stylelint-config-standard autoprefixer vitest jsdom @testing-library/react @vitest/ui @vitest/coverage-v8 sass --filter sample


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