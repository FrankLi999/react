/** @type {import('next').NextConfig} */
const { NextFederationPlugin } = require('@module-federation/nextjs-mf');
const webpack = require('webpack')
// const dotenv = require('dotenv');
// const { myEnv: environmentVars } = require('dotenv').config({
//     path:'/full/custom/path/to/env'
// })
// const { parsed: myEnv } = dotenv.config();
// console.log(">>>>>>>>>>>>> myEnv",  myEnv)

console.log(">>>>>>>>>>>>> NODE_ENV",  process.env.NODE_ENV)
console.log(">>>>>>>>>>>>> API_URL",  process.env.API_URL)
console.log(">>>>>>>>>>>>> ENV_VARIABLE1",  process.env.ENV_VARIABLE1)
console.log(">>>>>>>>>>>>> NEXT_PUBLIC_ENV_VARIABLE1",  process.env.NEXT_PUBLIC_ENV_VARIABLE1)
console.log(">>>>>>>>>>>>> BASE_URL",  process.env.BASE_URL)
console.log(">>>>>>>>>>>>> AMY_OS_VAR",  process.env.MY_OS_VAR)
console.log(">>>>>>>>>>>>> API_PARAM",  process.env.API_PARAM)
console.log(">>>>>>>>>>>>> API_URL_WITH_PARAM",  process.env.API_URL_WITH_PARAM)
console.log(">>>>>>>>>>>>> mf remote",  `nextjs-romote@${process.env.NEXTJS_REMOTE_UI}/_next/static/chunks/remoteEntry.js`)

const nextJsRemote = (isServer) => {
    return {
      "remote": `remote@${process.env.NEXTJS_REMOTE_UI}/_next/static/${isServer ? 'ssr' : 'chunks'}/remoteEntry.js` 
    }
}
module.exports = {
    // https://nextjs.org/docs/architecture/nextjs-compiler
    compiler: {
        styledComponents: true
    },
    compress: true,
    env: {
      BASE_URL: process.env.BASE_URL,
      API_URL: process.env.API_URL,
      NEXT_PUBLIC_ENV_VARIABLE1: process.env.NEXT_PUBLIC_ENV_VARIABLE1,
      API_URL_WITH_PARAM: process.env.API_URL_WITH_PARAM
    },
    output: 'standalone',
    reactStrictMode: true,
    // experimental: {
    //     // this includes files from the monorepo base two directories up
    //     outputFileTracingRoot: path.join(__dirname, '../../'),
    // },

    // runtime config is deprecated, We do not recommend using the runtimeConfig option, as this does not work with the standalone output mode. 
    // serverRuntimeConfig: {},
    // publicRuntimeConfig: {},
    //  Usually, this will be set in process.env but now it can be passed to the webpack.EnvironmentPlugin 
    // to do the replacements, like this
    // webpack(config) {
    //     config.plugins.push(new webpack.EnvironmentPlugin(myEnv))
    //     return config
    // }
    // webpack(config, options) {
    //   if (!options.isServer) {
    //     config.plugins.push(
    //       new NextFederationPlugin({
    //       name: 'nextjs-approuter',
    //           remotes: {
    //             remote: 'remote@http://localhost:8081/_next/static/chunks/remoteEntry.js',
    //             // remote: 'remote@http://localhost:8081/remoteEntry.js',
    //           },
    //           // remotes: nextJsRemote(options.issServer),
    //           shared: {},
    //           filename: 'static/chunks/remoteEntry.js',
    //       }),
    //     );
    //   }    
    //   return config;
    // }
}