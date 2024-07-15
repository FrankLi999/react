package mycamel.spring.boot.camel.support;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.util.StringUtils;

public class FeignApiRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ApiRequestClientRequestContext clientRequestContext = CamelRequestContextHolder.getApiRequestClientRequestContext();
        String traceId = clientRequestContext != null ? clientRequestContext.getTraceId() : null;
        if (StringUtils.hasText(traceId)) {
            template.header(MyCamelConstants.HEADER_MY_CAMEL_TRACE_ID, traceId);
        }
        String authToken = clientRequestContext != null ? clientRequestContext.getAuthToken() : null;
        if (StringUtils.hasText(authToken)) {
            template.header(MyCamelConstants.HEADER_MY_CAMEL_AUTH_TOKEN, authToken);
        }
    }
}
