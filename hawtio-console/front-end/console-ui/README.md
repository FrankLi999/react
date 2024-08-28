## DISABLE esline

set DISABLE_ESLINT_PLUGIN=true

export DISABLE_ESLINT_PLUGIN=true

## CRACO path alias

https://blog.logrocket.com/using-path-aliases-cleaner-react-typescript-imports/  
Create React App Configuration Override.

npm i -D @craco/craco

craco.config.js:

```
const path = require('path');
module.exports = {
  webpack: {
    alias: {
      '@': path.resolve(__dirname, 'src'),
    },
  },
};
```

script:

```
"scripts": {
-  "start": "react-scripts start"
+  "start": "craco start"
-  "build": "react-scripts build"
+  "build": "craco build"
-  "test": "react-scripts test"
+  "test": "craco test"
}
```

# dev

set PUBLIC_URL=/my-camel/admin/console
pnpm run start
http://localhost:3000/my-camel/admin/console

# Build

set PUBLIC_URL=/my-camel/admin/actuator/hawtio
pnpm run build

http://localhost:8080/my-camel/admin/actuator/hawtio

## rjsf/patternfly

ref:
https://github.com/kiegroup/kie-tools/tree/main/packages/uniforms-patternfly/src  
 https://github.com/rjsf-team/react-jsonschema-form/tree/main/packages/bootstrap-4
