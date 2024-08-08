const HtmlWebpackPlugin = require('html-webpack-plugin');
const ModuleFederationPlugin = require('webpack/lib/container/ModuleFederationPlugin');
const deps = require("./package.json").dependencies;
module.exports = {
  mode: 'development',
  entry: "./src/index",
  devServer: {
    port: 8082,
    open: true,
    headers: {
      "Access-Control-Allow-Origin": "*",
    },
  },
  resolve: {
    extensions: [".ts", ".tsx", ".js"],
  },
  module: {
    rules: [
      {
        /* The following line to ask babel 
             to compile any file with extension
             .js */
        // test: /\.js?$/,
        test: /\.(js|jsx|tsx|ts)?$/,
        /* exclude node_modules directory from babel. 
            Babel will not compile any files in this directory*/
        exclude: /node_modules/,
        use: {
          loader: "swc-loader",
          options: {
            jsc: {
              parser: {
                syntax: "typescript",
              }
            }
          }
        }        
        // To Use babel Loader
        // loader:
        //   'babel-loader',
        // options: {
        //   presets: [
        //     '@babel/preset-env' /* to transfer any advansed ES to ES5 */,
        //     '@babel/preset-react',
        //   ], // to compile react to ES5
        // },
      },
    ],
  },
  plugins: [
    new ModuleFederationPlugin(
      {
        name: 'MFE2',
        filename:
          'remoteEntry.js',
        shared: {
          ...deps,
          react: { 
            singleton: true, 
            eager: true, 
            requiredVersion: deps.react 
          },
          "react-dom": {
            singleton: true,
            eager: true,
            requiredVersion: deps["react-dom"],
          },
          "react-router-dom": {
            singleton: true,
            eager: true,
            requiredVersion: deps["react-router-dom"],
          },
        },  
        remotes: {
          MFE1:
            // 'MFE1@https://rany.tk/mfe/mfe1/dist/2021Feb27/remoteEntry.js',
            'MFE1@http://localhost:8083/remoteEntry.js',
        },
      }
    ),
    new HtmlWebpackPlugin({
      template:
        './public/index.html',
    }),
  ],
};  
  