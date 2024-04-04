
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

## API endpoints:

- /my/spring/admin/api/configurations
- /my/spring/admin/api/import
## Azure Oauth2 - Microsoft Entra ID
### create a new group


###. App registration

register the app 
redirect url: web
    https://www.baeldung.com/spring-boot-azuread-authenticate-users
    redirect url: /login/oauth2/code/azure-dev
client secret
token configuration: add group claim

### Enterprise application -> bind/assign user/group
    search registered app
    assign user/group

### token validation set up
https://learn.microsoft.com/en-us/answers/questions/1306114/microsoft-public-keys-only-validate-id-token-and-n

add custom scope: api://{app-id}/config
at app registrition -> manifest:
    line 4: "accessTokenAcceptedVersion": 2,
this way the issuer of the token will be: https://login.microsoftonline.com/{tenantid}/v2.0 other wise it will be
https://sts.windows.net/{tenantid}/, and not there is no signature validation error

### Azure logout


## Github
https://docs.github.com/en/developers/apps/building-oauth-apps/creating-an-oauth-app

github.client.clientId=[CLIENT_ID]
github.client.clientSecret=[CLIENT_SECRET]
github.client.userAuthorizationUri=https://github.com/login/oauth/authorize
github.client.accessTokenUri=https://github.com/login/oauth/access_token
github.client.clientAuthenticationScheme=form

github.resource.userInfoUri=https://api.github.com/user
github.resource.repoUri=https://api.github.com/user/repos

## Spring boot OAth2 as relay for SPA apps
https://www.baeldung.com/spring-cloud-gateway-bff-oauth2

## Oauth2 security best practise
JWTs were meant to be used within private networks, to reduce the number of requests to issuers.
They were supposed to be short lived (minutes) to execute one shot requests. They where never 
meant to replace sessions.
https://datatracker.ietf.org/doc/html/draft-ietf-oauth-security-topics

https://medium.com/@annaparissi_89713/creating-a-secure-spring-boot-react-app-using-okta-3f80ffc5fa6b

## Referesh token sample 

okta: 
   client id: 0oag0cd6kumjXO3Ws5d7

    okta refresh access token sample:
    http --form POST https://${yourOktaDomain}/oauth2/v1/token \
    accept:application/json \
    authorization:'Basic MG9hYmg3M...' \
    cache-control:no-cache \
    content-type:application/x-www-form-urlencoded \
    grant_type=refresh_token \
    redirect_uri=http://localhost:8080 \
    scope=offline_access%20openid \
    refresh_token=MIOf-U1zQbyfa3MUfJHhvnUqIut9ClH0xjlDXGJAyqo
## OCP Session affinity
for OCP Route static session:
route.openshift.io/cookie_name: JESSIONID

oc annotate route <route-name> route.openshift.io/cookie_name="<cookie_name>"

or for stateful Service - route to same pod from the 
spec.sessionAffinity: ClientIP
