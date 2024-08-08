import globals from "globals";
import pluginJs from "@eslint/js";
import tseslint from "typescript-eslint";
import pluginReact from "eslint-plugin-react";
import eslintPluginStorybook from "eslint-plugin-storybook";

export default [
  // {
  //   ignores: ["dist/**/*"],
  // },
  { files: ["**/*.{js,mjs,cjs,ts,jsx,tsx}"] },
  { settings: {
      react: {
        version: "detect",
      },
    }
  },
  { languageOptions: { globals: globals.browser } },
  pluginJs.configs.recommended,
  ...tseslint.configs.recommended,
  pluginReact.configs.flat.recommended,
  eslintPluginStorybook.configs.flat.recommended
];
