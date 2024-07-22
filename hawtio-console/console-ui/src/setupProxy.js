const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/my-camel/admin/console',
    createProxyMiddleware({
      target: 'http://localhost:8080/my-camel/admin/actuator/hawtio',
      changeOrigin: true,
      pathFilter: (path) => !(
        path.startsWith('/actuator/hawtio/static/') || 
        path.startsWith('/static/') || 
        path === '/favicon.ico' || 
        path === '/my-camel/admin/console/' ||
        path === '/my-camel/admin/console' ||
        path === '/' || 
        path === ''),
      pathRewrite: (path) => path.includes('\/jolokia\/') ? '/jolokia': path
      // pathRewrite: (path) => path === '/api/refreshConfig'? 'http://localhost:10000/my-camel/console/api/refreshConfig' : path.includes('\/jolokia\/') ? '/jolokia': path
    })
  );
  app.use(
    '/my-camel/admin/api',
    createProxyMiddleware({
      target: 'http://localhost:8080/my-camel/admin/api/refreshConfig',
      changeOrigin: true
    })
  );  
};