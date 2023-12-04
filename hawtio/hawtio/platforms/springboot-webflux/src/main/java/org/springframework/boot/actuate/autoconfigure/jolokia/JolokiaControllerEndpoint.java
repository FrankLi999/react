package org.springframework.boot.actuate.autoconfigure.jolokia;

import java.io.*;
import java.net.*;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.*;

import javax.management.RuntimeMBeanException;
import javax.security.auth.Subject;
import org.jolokia.backend.BackendManager;
import org.jolokia.config.*;
import org.jolokia.discovery.AgentDetails;
import org.jolokia.discovery.DiscoveryMulticastResponder;
import org.jolokia.restrictor.*;
import org.jolokia.util.*;
import org.json.simple.JSONAware;
import org.json.simple.JSONStreamAware;

import java.util.Map;
import java.util.function.Supplier;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpoint;
import jarkata.annotation.*;
import org.springframework.http.server.reactive.*;

@ControllerEndpoint(id = "jolokiaController")
public class JolokiaControllerEndpoint {
    private final Map<String, String> initParameters;
    // POST- and GET- HttpRequestHandler
    private ControllerRequestHandler httpGetHandler, httpPostHandler;
    // Backend dispatcher
    private BackendManager backendManager;

    // Used for logging
    private LogHandler logHandler;

    // Request handler for parsing request parameters and building up a response
    private ServerHttpRequestHandler requestHandler;

    // Restrictor to use as given in the constructor
    private Restrictor restrictor;

    // Mime type used for returning the answer
    private String configMimeType;

    // Listen for discovery request (if switched on)
    private DiscoveryMulticastResponder discoveryMulticastResponder;

    // whether to allow reverse DNS lookup for checking the remote host
    private boolean allowDnsReverseLookup;

    // whether to allow streaming mode for response
    private boolean streamingEnabled;

    /**
     * Constructor taking a restrictor to use
     *
     * @param pRestrictor restrictor to use or <code>null</code> if the restrictor
     *        should be created in the default way ({@link RestrictorFactory#createRestrictor(ControllerConfiguration,LogHandler)})
     */
	public JolokiaControllerEndpoint(Map<String, String> initParameters) {
		this.initParameters = initParameters;
	}

    public JolokiaControllerEndpoint(Map<String, String> initParameters, Restrictor pRestrictor) {
		this.initParameters = initParameters;
        thia.restrictor = pRestrictor;
	}

    @PostConstruct
    publiv void init() {
        ControllerConfiguration config = initConfig(initParameters);
        // Create a log handler early in the lifecycle, but not too early
        String logHandlerClass =  config.get(ConfigKey.LOGHANDLER_CLASS);
        logHandler = logHandlerClass != null ?
                (LogHandler) ClassUtil.newInstance(logHandlerClass) :
                createLogHandler(initParameters, Boolean.valueOf(config.get(ConfigKey.DEBUG)));

        // Different HTTP request handlers
        httpGetHandler = newGetHttpRequestHandler();
        httpPostHandler = newPostHttpRequestHandler();

        if (restrictor == null) {
            restrictor = createRestrictor(config);
        } else {
            logHandler.info("Using custom access restriction provided by " + restrictor);
        }
        configMimeType = config.get(ConfigKey.MIME_TYPE);

        addJsr160DispatcherIfExternallyConfigured(config);
        backendManager = new BackendManager(config, logHandler, restrictor);

        requestHandler = new HttpRequestHandler(config, backendManager, logHandler);
        allowDnsReverseLookup = config.getAsBoolean(ConfigKey.ALLOW_DNS_REVERSE_LOOKUP);
        streamingEnabled = config.getAsBoolean(ConfigKey.STREAMING);

        initDiscoveryMulticast(config);
    }

    /** {@inheritDoc} */
    @PreDestroy
    public void destroy() {
        backendManager.destroy();
        if (discoveryMulticastResponder != null) {
            discoveryMulticastResponder.stop();
            discoveryMulticastResponder = null;
        }
        super.destroy();
    }

    @GetMapping(value = "")
    public void doGet(ServerHttpRequest req, ServerHttpResponse resp)
            throws ServletException, IOException {
        handle(httpGetHandler, req, resp);
    }

    @PostMapping(value = "")
    protected void doPost(ServerHttpRequest req, ServerHttpResponse resp)
            throws ServletException, IOException {
        handle(httpPostHandler, req, resp);
    }

    // /**
    //  * OPTION requests are treated as CORS preflight requests
    //  *
    //  * @param req the original request
    //  * @param resp the response the answer are written to
    //  * */
    
    // protected void doOptions(ServerHttpRequest req, ServerHttpResponse resp) throws Exception {
    //     Map<String,String> responseHeaders =
    //             requestHandler.handleCorsPreflightRequest(
    //                     getOriginOrReferer(req),
    //                     req.getHeader("Access-Control-Request-Headers"));
    //     for (Map.Entry<String,String> entry : responseHeaders.entrySet()) {
    //         resp.setHeader(entry.getKey(),entry.getValue());
    //     }
    // }

    /**
     * Get the installed log handler
     *
     * @return loghandler used for logging.
     */
    protected LogHandler getLogHandler() {
        return logHandler;
    }

    // Examines servlet config and servlet context for configuration parameters.
    // ControllerConfiguration from the servlet context overrides servlet parameters defined in web.xml
    ControllerConfiguration initConfig(initParameters) {
        ControllerConfiguration config = new ControllerConfiguration(
                ConfigKey.AGENT_ID, NetworkUtil.getAgentId(hashCode(),"controller"));
        // From Spring properties ....
        config.updateGlobalConfiguration(new ControllerConfigFacade(initParameters));
        // Set type last and overwrite anything written
        config.updateGlobalConfiguration(Collections.singletonMap(ConfigKey.AGENT_TYPE.getKeyValue(),"controller"));
        return config;
    }

    // Implementation for the ServletConfig
    private static final class ControllerConfigFacade implements ControllerConfigExtractor {
        private final Map<String, String> initParameters;

        private ControllerConfigFacade(Map<String, String> initParameters) {
            this.initParameters = initParameters;
        }

        /** {@inheritDoc} */
        public Iterator<String> getNames() {
            return initParameters.keySet().iterator();
        }

        /** {@inheritDoc} */
        public String getParameter(String pName) {
            return initParameters.get(pName);
        }
    }

    /**
     * Create a log handler using this servlet's logging facility for logging. This method can be overridden
     * to provide a custom log handler. This method is called before {@link RestrictorFactory#createRestrictor(ControllerConfiguration,LogHandler)} so the log handler
     * can already be used when building up the restrictor.
     *
     * @return a default log handler
     * @param pServletConfig servlet config from where to get information to build up the log handler
     * @param pDebug whether to print out  debug information.
     */
    protected LogHandler createLogHandler(Map<String, String> initParameters, final boolean pDebug) {
        return new LogHandler() {
            /** {@inheritDoc} */
            public void debug(String message) {
                if (pDebug) {
                    log(message);
                }
            }

            /** {@inheritDoc} */
            public void info(String message) {
                log(message);
            }

            /** {@inheritDoc} */
            public void error(String message, Throwable t) {
                log(message,t);
            }
        };
    }

    /**
     * Hook for creating an own restrictor
     *
     * @param config configuration as given to the servlet
     * @return return restrictor or null if no restrictor is needed.
     */
    protected Restrictor createRestrictor(ControllerConfiguration config) {
        return RestrictorFactory.createRestrictor(config, logHandler);
    }

    // factory method for POST request handler
    private ControllerRequestHandler newPostHttpRequestHandler() {
        return new ControllerRequestHandler() {
            /** {@inheritDoc} */
             public JSONAware handleRequest(ServerHttpRequest pReq, ServerHttpResponse pResp)
                    throws IOException {
                 String encoding = pReq.getCharacterEncoding();
                 InputStream is = pReq.getInputStream();
                 return requestHandler.handlePostRequest(pReq.getURI(),is, encoding, getParameterMap(pReq));
             }
        };
    }

    // factory method for GET request handler
    private ControllerRequestHandler newGetHttpRequestHandler() {
        return new ControllerRequestHandler() {
            /** {@inheritDoc} */
            public JSONAware handleRequest(ServerHttpRequest pReq, ServerHttpResponse pResp) {
                return requestHandler.handleGetRequest(pReq.getURI(),pReq.getPath(), getParameterMap(pReq));
            }
        };
    }

    private interface ControllerRequestHandler {
        /**
         * Handle a request and return the answer as a JSON structure
         * @param pReq request arrived
         * @param pResp response to return
         * @return the JSON representation for the answer
         * @throws IOException if handling of an input or output stream failed
         */
        JSONAware handleRequest(ServerHttpRequest pReq, ServerHttpResponse pResp)
                throws IOException;
    }

    // Get parameter map either directly from an Servlet 2.4 compliant implementation
    // or by looking it up explictely (thanks to codewax for the patch)
    private Map<String, String[]> getParameterMap(ServerHttpRequest pReq){

        Map<String, String[]> result = getQueryParams().entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey, 
            e -> e.getValue().toArray(new String[e.getValue().size()])
        ));
    }

    /**
     * Add the JsrRequestDispatcher if configured via a system property or env variable.
     * The JSR160 dispatcher is disabled by default, but this allows to enable it again
     * without reconfiguring the controller
     *
     * @param pConfig configuration to update
     */
    private void addJsr160DispatcherIfExternallyConfigured(ControllerConfiguration pConfig) {
        String dispatchers = pConfig.get(ConfigKey.DISPATCHER_CLASSES);
        String jsr160DispatcherClass = "org.jolokia.jsr160.Jsr160RequestDispatcher";

        if (dispatchers == null || !dispatchers.contains(jsr160DispatcherClass)) {
            for (String param : new String[]{
                System.getProperty("org.jolokia.jsr160ProxyEnabled"),
                System.getenv("JOLOKIA_JSR160_PROXY_ENABLED")
            }) {
                if (param != null && (param.isEmpty() || Boolean.parseBoolean(param))) {
                    {
                        pConfig.updateGlobalConfiguration(
                            Collections.singletonMap(
                                ConfigKey.DISPATCHER_CLASSES.getKeyValue(),
                                (dispatchers != null ? dispatchers + "," : "") + jsr160DispatcherClass));
                    }
                    return;
                }
            }
            if (dispatchers == null) {
                // We add a breaking dispatcher to avoid silently ignoring a JSR160 proxy request
                // when it is now enabled
                pConfig.updateGlobalConfiguration(Collections.singletonMap(
                    ConfigKey.DISPATCHER_CLASSES.getKeyValue(),
                    Jsr160ProxyNotEnabledByDefaultAnymoreDispatcher.class.getCanonicalName()));
            }
        }
    }

    // Try to find an URL for system props or config
    private String findAgentUrl(ControllerConfiguration pConfig) {
        String url = getOrDefault(ConfigKey.DISCOVERY_AGENT_URL,
                                  "JOLOKIA_DISCOVERY_AGENT_URL",
                                  pConfig,
                                  String.class);
        return NetworkUtil.replaceExpression(url);
    }

    // For war agent needs to be switched on
    private boolean listenForDiscoveryMcRequests(ControllerConfiguration pConfig) {
        return getOrDefault(ConfigKey.DISCOVERY_ENABLED,
                            "JOLOKIA_DISCOVERY",
                            pConfig,
                            Boolean.class);
    }

    private <T> T getOrDefault(ConfigKey configKey, String sysEnvKey, ControllerConfiguration pConfig, Class<T> clazz) {

        // 1. As system property
        String property = System.getProperty("jolokia." + configKey.getKeyValue());
        if (property != null) {
            return getAsType(property, clazz);
        }

        // 2. As environment variable
        property = System.getenv(sysEnvKey);
        if (property != null) {
            return getAsType(property, clazz);
        }

        // 3. As configured valued
        property = pConfig.get(configKey);
        if (property != null) {
            return getAsType(property, clazz);
        }

        // 4. Default value
        return getAsType(configKey.getDefaultValue(), clazz);
    }

    private <T> T getAsType(String property, Class<T> clazz) {
        if (clazz == Integer.class) {
           return (T) Integer.valueOf(property);
        } else if (clazz == Boolean.class) {
            return (T) Boolean.valueOf(property);
        } else {
            return (T) property;
        }
    }

    private void initDiscoveryMulticast(ControllerConfiguration pConfig) {
        String url = findAgentUrl(pConfig);
        if (url != null || listenForDiscoveryMcRequests(pConfig)) {
            backendManager.getAgentDetails().setUrl(url);
            String multicastGroup = getOrDefault(ConfigKey.MULTICAST_GROUP, "JOLOKIA_MULTICAST_GROUP", pConfig, String.class);
            int multicastPort = getOrDefault(ConfigKey.MULTICAST_PORT, "JOLOKIA_MULTICAST_PORT", pConfig, Integer.class);
            try {
                discoveryMulticastResponder = new DiscoveryMulticastResponder(backendManager,restrictor,multicastGroup,multicastPort,logHandler);
                discoveryMulticastResponder.start();
            } catch (IOException e) {
                logHandler.error("Cannot start discovery multicast handler: " + e,e);
            }
        }
    }

    @SuppressWarnings({ "PMD.AvoidCatchingThrowable", "PMD.AvoidInstanceofChecksInCatchClause" })
    private void handle(ControllerRequestHandler pReqHandler,HttpServletRequest pReq, HttpServletResponse pResp) throws IOException {
        JSONAware json = null;

        try {
            // Check access policy
            requestHandler.checkAccess(allowDnsReverseLookup ? pReq.getRemoteHost() : null,
                                       pReq.getRemoteAddr(),
                                       getOriginOrReferer(pReq));

            // If a callback is given, check this is a valid javascript function name
            validateCallbackIfGiven(pReq);

            // Remember the agent URL upon the first request. Needed for discovery
            updateAgentDetailsIfNeeded(pReq);

            // Dispatch for the proper HTTP request method
            json = handleSecurely(pReqHandler, pReq, pResp);
        } catch (Throwable exp) {
            try {
                json = requestHandler.handleThrowable(
                    exp instanceof RuntimeMBeanException ? ((RuntimeMBeanException) exp).getTargetException() : exp);
            } catch (Throwable exp2) {
                exp2.printStackTrace();
            }
        } finally {
            setCorsHeader(pReq, pResp);

            if (json == null) {
                json = requestHandler.handleThrowable(new Exception("Internal error while handling an exception"));
            }

            sendResponse(pResp, pReq, json);
        }
    }


    private JSONAware handleSecurely(final ControllerRequestHandler pReqHandler, final HttpServletRequest pReq, final HttpServletResponse pResp) throws IOException, PrivilegedActionException {
        Subject subject = (Subject) pReq.getAttribute(ConfigKey.JAAS_SUBJECT_REQUEST_ATTRIBUTE);
        if (subject != null) {
            return Subject.doAs(subject, new PrivilegedExceptionAction<JSONAware>() {
                    public JSONAware run() throws IOException {
                        return pReqHandler.handleRequest(pReq, pResp);
                    }
            });
        } else {
            return pReqHandler.handleRequest(pReq, pResp);
        }
    }

    private String getOriginOrReferer(HttpServletRequest pReq) {
        String origin = pReq.getHeader("Origin");
        if (origin == null) {
            origin = pReq.getHeader("Referer");
        }
        return origin != null ? origin.replaceAll("[\\n\\r]*","") : null;
    }


    // Update the agent URL in the agent details if not already done
    private void updateAgentDetailsIfNeeded(HttpServletRequest pReq) {
        // Lookup the Agent URL if needed
        AgentDetails details = backendManager.getAgentDetails();
        if (details.isInitRequired()) {
            synchronized (details) {
                if (details.isInitRequired()) {
                    if (details.isUrlMissing()) {
                        String url = getBaseUrl(NetworkUtil.sanitizeLocalUrl(pReq.getRequestURL().toString()),
                                extractServletPath(pReq));
                        details.setUrl(url);
                    }
                    if (details.isSecuredMissing()) {
                        details.setSecured(pReq.getAuthType() != null);
                    }
                    details.seal();
                }
            }
        }
    }

    private String extractServletPath(HttpServletRequest pReq) {
        return pReq.getRequestURI().substring(0,pReq.getContextPath().length());
    }

    // Strip off everything unneeded
    private String getBaseUrl(String pUrl, String pServletPath) {
        String sUrl;
        try {
            URL url = new URL(pUrl);
            String host = getIpIfPossible(url.getHost());
            sUrl = new URL(url.getProtocol(),host,url.getPort(),pServletPath).toExternalForm();
        } catch (MalformedURLException exp) {
            sUrl = plainReplacement(pUrl, pServletPath);
        }
        return sUrl;
    }

    // Check for an IP, since this seems to be safer to return then a plain name
    private String getIpIfPossible(String pHost) {
        try {
            InetAddress address = InetAddress.getByName(pHost);
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            return pHost;
        }
    }

    // Fallback used if URL creation didnt work
    private String plainReplacement(String pUrl, String pServletPath) {
        int idx = pUrl.lastIndexOf(pServletPath);
        String url;
        if (idx != -1) {
            url = pUrl.substring(0,idx) + pServletPath;
        } else {
            url = pUrl;
        }
        return url;
    }

    // Set an appropriate CORS header if requested and if allowed
    private void setCorsHeader(HttpServletRequest pReq, HttpServletResponse pResp) {
        String origin = requestHandler.extractCorsOrigin(pReq.getHeader("Origin"));
        if (origin != null) {
            pResp.setHeader("Access-Control-Allow-Origin", origin);
            pResp.setHeader("Access-Control-Allow-Credentials","true");
        }
    }

    private boolean isStreamingEnabled(HttpServletRequest pReq) {
        String streamingFromReq = pReq.getParameter(ConfigKey.STREAMING.getKeyValue());
        if (streamingFromReq != null) {
            return Boolean.parseBoolean(streamingFromReq);
        }
        return streamingEnabled;
    }

    private void sendResponse(HttpServletResponse pResp, HttpServletRequest pReq, JSONAware pJson) throws IOException {
        String callback = pReq.getParameter(ConfigKey.CALLBACK.getKeyValue());

        setContentType(pResp,
                       MimeTypeUtil.getResponseMimeType(
                           pReq.getParameter(ConfigKey.MIME_TYPE.getKeyValue()),
                           configMimeType, callback));
        pResp.setStatus(HttpServletResponse.SC_OK);
        setNoCacheHeaders(pResp);
        if (pJson == null) {
            pResp.setContentLength(-1);
        } else {
            if (isStreamingEnabled(pReq)) {
                sendStreamingResponse(pResp, callback, (JSONStreamAware) pJson);
            } else {
                // Fallback, send as one object
                // TODO: Remove for 2.0 where should support only streaming
                sendAllJSON(pResp, callback, pJson);
            }
        }
    }

    private void validateCallbackIfGiven(HttpServletRequest pReq) {
        String callback = pReq.getParameter(ConfigKey.CALLBACK.getKeyValue());
        if (callback != null && !MimeTypeUtil.isValidCallback(callback)) {
            throw new IllegalArgumentException("Invalid callback name given, which must be a valid javascript function name");
        }
    }
    private void sendStreamingResponse(HttpServletResponse pResp, String pCallback, JSONStreamAware pJson) throws IOException {
        Writer writer = new OutputStreamWriter(pResp.getOutputStream(), "UTF-8");
        IoUtil.streamResponseAndClose(writer, pJson, pCallback);
    }

    private void sendAllJSON(HttpServletResponse pResp, String callback, JSONAware pJson) throws IOException {
        OutputStream out = null;
        try {
            String json = pJson.toJSONString();
            String content = callback == null ? json : callback + "(" + json + ");";
            byte[] response = content.getBytes("UTF8");
            pResp.setContentLength(response.length);
            out = pResp.getOutputStream();
            out.write(response);
        } finally {
            if (out != null) {
                // Always close in order to finish the request.
                // Otherwise the thread blocks.
                out.close();
            }
        }
    }

    private void setNoCacheHeaders(HttpServletResponse pResp) {
        pResp.setHeader("Cache-Control", "no-cache");
        pResp.setHeader("Pragma","no-cache");
        // Check for a date header and set it accordingly to the recommendations of
        // RFC-2616 (http://tools.ietf.org/html/rfc2616#section-14.21)
        //
        //   "To mark a response as "already expired," an origin server sends an
        //    Expires date that is equal to the Date header value. (See the rules
        //  for expiration calculations in section 13.2.4.)"
        //
        // See also #71

        long now = System.currentTimeMillis();
        pResp.setDateHeader("Date",now);
        // 1h  in the past since it seems, that some servlet set the date header on their
        // own so that it cannot be guaranteed that these headers are really equals.
        // It happened on Tomcat that Date: was finally set *before* Expires: in the final
        // answers some times which seems to be an implementation peculiarity from Tomcat
        pResp.setDateHeader("Expires",now - 3600000);
    }

    private void setContentType(HttpServletResponse pResp, String pContentType) {
        boolean encodingDone = false;
        try {
            pResp.setCharacterEncoding("utf-8");
            pResp.setContentType(pContentType);
            encodingDone = true;
        }
        catch (NoSuchMethodError error) { /* Servlet 2.3 */ }
        catch (UnsupportedOperationException error) { /* Equinox HTTP Service */ }
        if (!encodingDone) {
            // For a Servlet 2.3 container or an Equinox HTTP Service, set the charset by hand
            pResp.setContentType(pContentType + "; charset=utf-8");
        }
    }
}
