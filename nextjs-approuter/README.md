# i18next with app router
https://locize.com/blog/next-app-dir-i18n/
https://i18nexus.com/tutorials/nextjs/react-i18next
https://github.com/i18next/next-app-dir-i18next-example-ts
https://locize.com/blog/next-app-dir-i18n/
https://github.com/i18next/next-app-dir-i18next-example-ts.git
This is a [Next.js](https://nextjs.org/) project bootstrapped with [`create-next-app`](https://github.com/vercel/next.js/tree/canary/packages/create-next-app).


## env
https://medium.com/courtly-intrepid/environmental-variables-in-next-js-with-dotenv-599c5bbfdf74

https://dev.to/dipakkr/best-practises-for-loading-environment-variable-in-nextjs-application-3hni#:~:text=Here%20are%20the%20best%20practices,key%20vaults%2C%20or%20configuration%20files.

To expose a variable to the browser, the variable had to be prefixed with “NEXT_PUBLIC”.

- process.env
- env.${NODE_ENV}.local
- env.local (env.local is not checked when NODE_ENV is test)
- env.${NODE_ENV}
- .env

runtimeConfig options are deprecared and it does not work the standalone output mode.

## styled component
    https://github.com/Pedro-Estevao/boilerplate-nextjs
    https://www.mohammadfaisal.dev/blog/how-to-setup-styled-components-in-nextjs
    https://github.com/pacocoursey/next-themes
        npm install next-themes
    server side support: 
         https://nextjs.org/docs/architecture/nextjs-compiler
module.exports = {
  compiler: {
    styledComponents: true
  }
}

    or
        npm install --save-dev babel-plugin-styled-components

    or 
        in next-config.js

            const withPlugins = require("next-compose-plugins");
            const withTM = require("next-transpile-modules")(["styled-components"]);

            module.exports = withPlugins([withTM], {
                webpack(config, options) {
                    return config;
                },
            });
        
### work with app router

- lib/registry.tsx - first, use the styled-components API to create a global registry component to collect all CSS style rules generated during a render, and a function to return those rules. Then use the useServerInsertedHTML hook to inject the styles collected in the registry into the <head> HTML tag in the root layout.

- app/layout.tsx - Wrap the children of the root layout with the style registry component



## module federation

https://github.com/module-federation/module-federation-examples/tree/master/react-nextjs/nextjs-host-remote

https://github.com/OmerHerera/module-federation-post

issue with App router:
    https://github.com/module-federation/universe/issues/1183

npm install module-federation/nextjs-mf

## API
App router:
    If you are using the App Router, you can use Server Components or Route Handlers instead of API Routes.

Page Router:
    Any file inside the folder pages/api is mapped to /api/* and will be treated as an API endpoint instead of a page. 

## ORM

https://github.com/georgwittberger/next-app-router-template
https://create.t3.gg/
https://next-auth.js.org/
https://www.prisma.io/
https://trpc.io/

## openshift confmap to env file

config map:  
    apiVersion: v1
    name: my-env-local
    data:
    .env: |-
        NEXT_PUBLIC_API_URL=http:/your.domain.com/api
        API_URL=http://another.endpoint.com/serverSide
    kind: ConfigMap

depoyment:

    apiVersion: apps/v1
kind: Deployment
spec:
  replicas: 1
  selector:
    matchLabels:
      app: your-app
  template:
    metadata:
      labels:
        app: your-app
    spec:
      containers:
      - image: your/image:latest
        imagePullPolicy: Always
        name: your-app
        ports:
        volumeMounts:
        - mountPath: /app/.env.local
          name: env-local
          readOnly: true
          subPath: .env.local
      volumes:
      - configMap:
          defaultMode: 420
          items:
          - key: .env
            path: .env.local
          name: my-env-local
        name: env-local