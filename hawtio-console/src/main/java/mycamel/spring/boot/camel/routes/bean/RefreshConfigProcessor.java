package mycamel.spring.boot.camel.routes.bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import mycamel.spring.boot.camel.dto.*;
import mycamel.spring.boot.camel.routes.config.RefreshConfigService;
import mycamel.spring.boot.camel.support.CamelUtils;
import mycamel.spring.boot.camel.support.MyCamelConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class RefreshConfigProcessor {
    // @Value("${my-camel.refresh-url}")
    // private List<RefreshConfig> defaultConfifurations = new ArrayList<>();

    private final CamelUtils camelUtils;
    private final RefreshConfigService refreshConfigService;
    public void setupRequestContext(Exchange exchange) {
        CamelRequest<RefreshConfigRequest> camelRequest = this.camelUtils.processRequest(exchange, RefreshConfigRequest.class);
        exchange.getMessage().setHeader(MyCamelConstants.HEADER_MY_CAMEL_AUTH_TOKEN, camelRequest.getRequestContext().getAuthToken());

        RefreshConfigRequest configRequest = camelRequest.getRequestBody();
        RequestContext requestContext = camelRequest.getRequestContext();
//        if (CollectionUtils.isEmpty(configRequest.getRefreshConfigurations())) {
//            configRequest.setRefreshConfigurations(defaultConfifurations);
//        }
        exchange.getMessage().setBody(configRequest);
    }

    public void createApiResponse(Exchange exchange) {
        Map<String, Object> requestContextMap = exchange.getProperty(MyCamelConstants.PROPERTY_MY_CAMEL_CONTEXT_MAP, Map.class);

        Map<String, Set<String>>  resp = exchange.getMessage().getBody(Map.class);
        RequestContext requestContext = (RequestContext) requestContextMap.get(MyCamelConstants.PROPERTY_MY_CAMEL_CONTEXT_MAP);
        if (null == requestContext.getStatus()) {
            requestContext.setStatus(HttpStatus.OK);
        }
        String responseJson = camelUtils.serializePayload(resp, log);
        CamelResponse<String> camelResponse = CamelResponse.<String>builder()
                .requestContext(requestContext)
                .responseBody(responseJson)
                .build();
        if (log.isDebugEnabled()) {
            camelUtils.logApiResponsePayload(log, "MyCamel:API_RESPONSE:RefreshConfig (Exit)", requestContext.getRequestHeader().getTraceId(), responseJson);
        }
        exchange.getMessage().setBody(camelResponse);
        exchange.removeProperty(MyCamelConstants.PROPERTY_MY_CAMEL_CONTEXT_MAP);
        camelUtils.removeRequestContext();

    }

    public void refreshConfig(Exchange exchange)  throws JsonProcessingException {
        RefreshConfig refreshConfig = exchange.getMessage().getBody(RefreshConfig.class);
        Set<String> propertiesUpdates = refreshConfigService.refresh(refreshConfig.getRefreshUrl());
        Map<String, Set<String>> resp = new LinkedHashMap<>();
        resp.put(StringUtils.hasText(refreshConfig.getPod()) ? refreshConfig.getPod() : refreshConfig.getRefreshUrl(), propertiesUpdates);
        exchange.getMessage().setBody(resp);
    }

    public List<RefreshConfig> splitRefreshConfig(Exchange exchange) {
        RefreshConfigRequest refreshConfigRequest = exchange.getMessage().getBody(RefreshConfigRequest.class);
        List<RefreshConfig> refreshConfigs = new ArrayList<>();
        refreshConfigs.addAll(refreshConfigRequest.getRefreshConfigurations());
        return refreshConfigs;
    }


}
