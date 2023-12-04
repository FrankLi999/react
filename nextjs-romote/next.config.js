/** @type {import('next').NextConfig} */
const { NextFederationPlugin } = require('@module-federation/nextjs-mf');
module.exports = {
    async headers() {
      return [
        {
            source: '/api/quiery',
            headers: [
                {
                key: 'Access-Control-Allow-Origin',
                value: '*',
                },
                {
                key: 'x-another-custom-header',
                value: 'my other custom header value',
                }
            ]
        },
      ]
    },
    output: 'standalone',
    reactStrictMode: true,
    webpack(config, options) {
        Object.assign(config.experiments, { topLevelAwait: true });

        if (!options.isServer) {
            config.plugins.push(
              new NextFederationPlugin({
                  name: 'myromote',
                  exposes: {
                      './nextjs-remote-component': '@/app/page',
                  },
                  shared: {},
                  filename: 'static/chunks/remoteEntry.js',
              }),
          );
        }
        return config;
    },
}
