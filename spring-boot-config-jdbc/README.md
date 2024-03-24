
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


## Spring security Filter

   DelegatingFilterProxy(BeanFilter) -> FilterChainProxy -> SecurityFilterChain (one per path matcher) -> Security Filters

## Security filters

  Instead of implementing Filter, you can extend from OncePerRequestFilter which is a base class for filters that are only
  invoked once per request and provides a doFilterInternal method with the HttpServletRequest and HttpServletResponse parameters.


   CsrfFilter  
   UserNamePasswordAuthenticationFilter  
   BasicAuthenticationFilter  
   AuthorizationFilter  

    The list of filters is printed at INFO level on the application startup, so you can see something like the following on the console output for example:
   ```
    org.springframework.security.web.session.DisableEncodeUrlFilter@404db674,
    org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@50f097b5,
    org.springframework.security.web.context.SecurityContextHolderFilter@6fc6deb7,
    org.springframework.security.web.header.HeaderWriterFilter@6f76c2cc,
    org.springframework.security.web.csrf.CsrfFilter@c29fe36,
    org.springframework.security.web.authentication.logout.LogoutFilter@ef60710,
    org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter@7c2dfa2,
    org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter@4397a639,
    org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter@7add838c,
    org.springframework.security.web.authentication.www.BasicAuthenticationFilter@5cc9d3d0,
    org.springframework.security.web.savedrequest.RequestCacheAwareFilter@7da39774,
    org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@32b0876c,
    org.springframework.security.web.authentication.AnonymousAuthenticationFilter@3662bdff,
    org.springframework.security.web.access.ExceptionTranslationFilter@77681ce4,
    org.springframework.security.web.access.intercept.AuthorizationFilter@169268a7]
   ```
### Handling Security Exceptions
    The ExceptionTranslationFilter allows translation of AccessDeniedException and AuthenticationException into HTTP responses.


    1. First, the ExceptionTranslationFilter invokes FilterChain.doFilter(request, response) to invoke the rest of the application.

    2. If the user is not authenticated or it is an AuthenticationException, then Start Authentication.

        The SecurityContextHolder is cleared out.
        
        The HttpServletRequest is saved so that it can be used to replay the original request once authentication is successful.
        
        The AuthenticationEntryPoint is used to request credentials from the client. For example, it might redirect to a log in page or send a WWW-Authenticate header.

    3 Otherwise, if it is an AccessDeniedException, then Access Denied. The AccessDeniedHandler is invoked to handle access denied.

## Authentication
### Big picture
   
    SecurityContextHolder/SecurityContext
    Authentication: input to AutheticationManager with user credentials
    GrantedAuthority
    AutheticationManager: api define how spring security filters perform authetication
             authenticate(Authentication)
    ProviderManager: the most common implementation of AuthenticationManager.
    AuthenticationProvider - used by ProviderManager to perform a specific type of authentication.
    AuthenticationEntryPoint -  used to send an HTTP response that requests credentials from a client.
    AbstractAuthenticationProcessingFilter - a base Filter used for authentication. This also gives a good idea of the high level flow of authentication 
       and how pieces work together.
### AbstractAuthenticationProcessingFilter
    1. create Authentication
    2. AuthenticationManafer.authenticate(Authentication)
    3. if failed, clear securityContextHolder, RememberMeServices.loginFail is invokeed, invoke AuthenticationFailureHandler
    4. If auth successful, 
          notify SessionAuthenticationStrategy of new login
          Set Authentication to SecurityContextHolder. Later, if you need to save the SecurityContext so that it can be automatically set on future requests, 
                SecurityContextRepository#saveContext must be explicitly invoked. See the SecurityContextHolderFilter class.
          RememberMeServices.loginSuccessful
          ApplicatonEventPublisher publish InteractiveAuthenticationSuccessEvent
          AuthenticationSuccessHandler is invoked.
          
### Persisting Authentication - SecurityContextRepository
    In Spring Security the association of the user to future requests is made using SecurityContextRepository. The default implementation of 
    SecurityContextRepository is DelegatingSecurityContextRepository which delegates to the following:

        HttpSessionSecurityContextRepository: associates the SecurityContext to the HttpSession.
        
        RequestAttributeSecurityContextRepository
        
### OAuth2 login
    CommonOAuth2Provider pre-defines a set of default client properties for a number of well known providers: Google, GitHub, Facebook, and Okta.

#### Initial Setup
    Google
    https://developers.google.com/identity/protocols/OpenIDConnect

#### redirect url
     The default redirect URI template is {baseUrl}/login/oauth2/code/{registrationId}. The registrationId is a 
        unique identifier for the ClientRegistration.
     such as, localhost:8080/login/oauth2/code/google.

### config client
	spring.security.oauth2.client.registration is the base property prefix for OAuth Client properties
    Following the base property prefix is the ID for the ClientRegistration, such as Google.


     spring:
        security:
          oauth2:
             client:
               registration:
                 # registration id
                 google:
                   client-id: google-client-id
                   client-secret: google-client-secret

     or 

     spring:
       security:
         oauth2:
           client:
             registration:
               # registration id
               google-login:
                 provider: google
                 client-id: google-client-id
                 client-secret: google-client-secret
     or with custom provider properties
      spring:
        security:
          oauth2:
            client:
              registration:
                okta:
                  client-id: okta-client-id
                  client-secret: okta-client-secret
              provider:
                okta:
                  authorization-uri: https://your-subdomain.oktapreview.com/oauth2/v1/authorize
                  token-uri: https://your-subdomain.oktapreview.com/oauth2/v1/token
                  user-info-uri: https://your-subdomain.oktapreview.com/oauth2/v1/userinfo
                  user-name-attribute: sub
                  jwk-set-uri: https://your-subdomain.oktapreview.com/oauth2/v1/keys

### OAuth client

Authorization Grant support
    Authorization Code    
    Refresh Token    
    Client Credentials    
    Resource Owner Password Credentials    
    JWT Bearer

```
@Configuration
@EnableWebSecurity
public class OAuth2ClientSecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.oauth2Client(oauth2 -> oauth2
				.clientRegistrationRepository(this.clientRegistrationRepository())
				.authorizedClientRepository(this.authorizedClientRepository())
				.authorizedClientService(this.authorizedClientService())
				.authorizationCodeGrant(codeGrant -> codeGrant
					.authorizationRequestRepository(this.authorizationRequestRepository())
					.authorizationRequestResolver(this.authorizationRequestResolver())
					.accessTokenResponseClient(this.accessTokenResponseClient())
				)
			);
		return http.build();
	}
}
```
### JWT token


## TODO

remember me
    https://docs.spring.io/spring-security/reference/servlet/authentication/rememberme.html
    
    Remember-me or persistent-login authentication refers to web sites being able to remember the identity of a principal between sessions
    
     This is typically accomplished by sending a cookie to the browser, with the cookie being detected during future sessions and causing automated login to take place. 

SessionAuthenticationStrategy  
jwt token mechanism/refresh token
session management:
    https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html
logout


