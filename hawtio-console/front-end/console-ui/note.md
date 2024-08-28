## Template

https://github.com/varbouk/cra-sass-eslint-stylelint-prettier

## es lint

### out of the box CRA

automatically in package.json

```
"eslintConfig": {
    "extends": [
      "react-app",
      "react-app/jest",
      "plugin:jsx-a11y/recommended"
    ],
    "plugins": ["jsx-a11y"]
  }
```

By default, CRA running in development mode (npm run start) will only notify you of ESLint issues set by the react-app rules.

In essence, the issues you see in the console during development may be different than the ones you see if you ran eslint --ext .ts,.tsx src/.

If you prefer to have them synchronized, create a .env file in your project's root directory and add EXTEND_ESLINT=true.

```
// .env

EXTEND_ESLINT=true
```

Optionally Add npm Lint Script

```
// package.json

{
  // ...
  "scripts": {
    // ...
    "lint": "eslint --ext .ts,.tsx src/"
  }
}
```

### VS Code ESLint extention

### Manually

if done manually,  
 npm i -D eslint@^6.6.0 eslint-plugin-react @typescript-eslint/eslint-plugin @typescript-eslint/parser

     in .eslintrc.js

```
// .eslintrc.js
module.exports = {
  env: {
      browser: true, // Allows for the use of predefined global variables for browsers (document, window, etc.)
      jest: true, // Allows for the use of predefined global variables for Jest (describe, test, etc.)
      node: true, // Allows for the use of predefined global variables for Node.js (module, process, etc.)
  },
  extends: [
      "react-app", // Use the recommended rules from eslint-config-react-app (bundled with Create React App)
      "eslint:recommended", // Use the recommened rules from eslint
      "plugin:@typescript-eslint/recommended", // Use the recommended rules from @typescript-eslint/eslint-plugin
      "plugin:react/recommended", // Use the recommended rules from eslint-plugin-react
  ],
  parser: "@typescript-eslint/parser", // Specifies the ESLint parser
  parserOptions: {
      ecmaVersion: 2020, // Allows for the parsing of modern ECMAScript features
      ecmaFeatures: {
          jsx: true // Allows for the parsing of JSX
      },
      sourceType: "module", // Allows for the use of imports
  },
  plugins: [
      "@typescript-eslint", // Allows for manually setting @typescript-eslint/* rules
      "react", // Allows for manually setting react/* rules
  ],
  settings: {
      react: {
          version: "detect" // Tells eslint-plugin-react to automatically detect the version of React to use
      },
  },
};
```

### Extends vs. Plugins

Extends uses a configuration file to apply a set of predefined rules.

Plugins provide a set of rules that a user can toggle. Having a plugin does not automatically add any rules, you will need to enable them manually.

## prettier

npm i -D prettier eslint-config-prettier eslint-plugin-prettier

Create Prettier Configuration File

```
// .prettierrc.js

module.exports = {
  semi: true,
  printWidth: 100,
  singleQuote: true,
  jsxSingleQuote: true,
  singleAttributePerLine: true,
  tabWidth: 2,
  trailingComma: "es5"
};
```

.prettierignore

```
dist
node_modules
.github
.changeset

```

in eslint rc, add these extend:

```
 "prettier"
 "plugin:prettier/recommended", // Enables eslint-plugin-prettier to display Prettier errors as ESLint errors
```

add this plugin,

```
"prettier", // Allows for manually setting prettier/* rules
```

update package.json and add these scripts:

```
"format": "prettier --check .",
"format:fix": "prettier --write ."
```

## sass style lint and postcss
https://esausilva.com/2020/07/23/how-to-use-sass-and-css-modules-with-create-react-app-cra/  
pnpm i -D sass --filter console-ui

pnpm i -D stylelint stylelint-config-prettier stylelint-config-standard stylelint-config-standard-scss stylelint-prettier stylelint-scss --filter console-ui

When you build the app, create-react-app will use PostCSS with the Autoprefixer plugin to automatically add the required vendor prefixes.
This works with the browsers list in your package.json. 


.stylelintrc.js  
.stylelintignore  

## unit test

https://create-react-app.dev/docs/running-tests/  

## story book

## husky and lint staged

npm i -D husky lint-staged --filter console-ui  

in package.json,
```
"husky": {
    "hooks": {
      "pre-commit": "lint-staged"
    }
  },
  "lint-staged": {
    "**/*.{js,jsx}": [
      "prettier --write",
      "eslint --fix",
      "git add"
    ],
    "**/*.{css,scss}": [
      "prettier --write",
      "stylelint --fix",
      "git add"
    ]
  },
```