
## Proxy setting for local development

in package.json (if change it, remove the package.json.lock file)

"proxy": "http://localhost:8888",

## URLs
servlet.context-path: /s2i-integrator/config

actuator:
http://localhost:8888/my/actuator

http://localhost:8888/my/s2i-ccpay-integrator/default

http://localhost:8888/my/spring/admin/api/configurations

http://localhost:3000/my/spring/admin/dashboard/integrator/configuration-data

http://localhost:8888/my/spring/admin/dashboard/integrator/configuration-data


## React build

## `npm run build`
set PUBLIC_URL=/my/spring/admin/dashboard
npm run build


## React route:

var routes = createBrowserRouter([
   ...
], {
    basename: "/my/spring/admin/dashboard",
});