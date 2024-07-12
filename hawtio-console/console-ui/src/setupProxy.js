const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/',
    createProxyMiddleware({
      target: 'http://localhost:10001/actuator/hawtio',
      changeOrigin: true,
      pathFilter: (path) => !(path.startsWith('/actuator/hawtio/static/') || path.startsWith('/static/') || path === '/favicon.ico' || path === '/' || path === ''),
      pathRewrite: (path) => path.includes('\/jolokia\/') ? '/jolokia': path
    })
  );
};