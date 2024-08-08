import resolve from '@rollup/plugin-node-resolve';
import commonjs from '@rollup/plugin-commonjs';
import typescript from '@rollup/plugin-typescript';
import peerDepsExternal from 'rollup-plugin-peer-deps-external';
import image from '@rollup/plugin-image';
import postcss from 'rollup-plugin-postcss';
import del from 'rollup-plugin-delete';
import { terser } from 'rollup-plugin-terser'; //minify
import visualizer from 'rollup-plugin-visualizer';
const packageJson = require("./package.json");

export default [{
  input: [
    'src/index.ts'
  ],
  output: [{
    file: packageJson.main,
    format: "cjs",
    sourcemap: true,
  },
  {
    file: packageJson.module,
    format: "esm",
    sourcemap: true,
  }],
  plugins: [
    del({targets: 'dist/*'}),
    peerDepsExternal(),
    resolve(),
    commonjs(),
    typescript(),
    terser(),
    postcss({
      extract: false,
      modules: true,
      use: ['sass'],
    }),
    image(),
    visualizer()
  ],
  external: ["react", "react-dom"],
},
{
  input: "src/index.ts",
  output: [{ file: packageJson.types, format: "esm" }],
  external: [/\.(css|less|scss)$/],
},
];
