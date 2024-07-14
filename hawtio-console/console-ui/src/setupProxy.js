const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/',
    createProxyMiddleware({
      target: 'http://localhost:10001/my-camel/console/actuator/hawtio',
      changeOrigin: true,
      pathFilter: (path) => !(path.startsWith('/actuator/hawtio/static/') || path.startsWith('/static/') || path === '/favicon.ico' || path === '/' || path === ''),
      pathRewrite: (path) => path.includes('\/jolokia\/') ? '/jolokia': path
      // pathRewrite: (path) => path === '/api/refreshConfig'? 'http://localhost:10000/my-camel/console/api/refreshConfig' : path.includes('\/jolokia\/') ? '/jolokia': path
    })
  );
  // app.use(
  //   '/api/refreshConfig',
  //   createProxyMiddleware({
  //     target: 'http://localhost:10000/my-camel/console/api/refreshConfig',
  //     changeOrigin: true
  //   })
  // );  
};