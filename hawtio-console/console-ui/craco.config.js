const path = require('path');
module.exports = {
  webpack: {
    alias: {
      '@react-jsf/utils': path.resolve(__dirname, 'src/lib/utils/src/index.ts'),
      '@react-jsf/ajv': path.resolve(__dirname, 'src/lib/ajv/src/index.ts'),
      '@react-jsf/core': path.resolve(__dirname, 'src/lib/core/src/index.ts'),
      '@react-jsf/patternfly': path.resolve(__dirname, 'src/lib/patternfly/src/index.ts'),
    },
  },
};