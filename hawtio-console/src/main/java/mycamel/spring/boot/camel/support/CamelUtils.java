package mycamel.spring.boot.camel.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mycamel.spring.boot.camel.dto.CamelRequest;
import mycamel.spring.boot.camel.dto.CamelResponse;
import mycamel.spring.boot.camel.exception.UnexpectedError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CamelUtils {
    private final ProducerTemplate producerTemplate;
    private final ObjectMapper objectMapper;
    public ResponseEntity<String> processCamelRequest(CamelRequest camelRequest, String camelRoute) {
        Map<String, Object> headers = new HashMap<>();
        headers.put(MyCamelConstants.HEADER_MY_CAMEL_AUTH_TOKEN, camelRequest.getRequestContext().getAuthToken());
        headers.put(MyCamelConstants.HEADER_MY_CAMEL_TRACE_ID, camelRequest.getRequestContext().getRequestHeader().getTraceId());
        CamelResponse<String> camelResponse = producerTemplate.requestBodyAndHeaders(camelRoute, camelRequest, headers, CamelResponse.class);
        return ResponseEntity.status(camelResponse.getRequestContext().getStatus()).body(camelResponse.getResponseBody());
    }

    public void removeRequestContext() {
        CamelRequestContextHolder.resetApiClientRequestContext();
        LogHelper.removeMdc();
    }

    public <T> CamelRequest<T> processRequest(Exchange exchange, Class<T> type) {
        CamelRequest<T> camelRequest = exchange.getMessage().getBody(CamelRequest.class);
        exchange.getMessage().setBody(camelRequest.getRequestJson());
        Map<String, Object> requestContextMap = new LinkedHashMap<>();
        requestContextMap.put(MyCamelConstants.PROPERTY_MY_CAMEL_CONTEXT_MAP, camelRequest.getRequestContext());
        exchange.setProperty(MyCamelConstants.PROPERTY_MY_CAMEL_CONTEXT_MAP, requestContextMap);
        return camelRequest;
    }

    public String serializePayload(Object payload, Logger log) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error("Error serialize payload", e);
            if (log.isDebugEnabled()) {
                log.debug(String.format("Error serialize payload %s", payload));
            }
            throw new UnexpectedError("Failed to serialize json. " + e.getMessage(), e);
        }
    }

    public void logApiRequestPayload(Logger log, String logSubject, String traceId, Object payload) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("LogSubject: %s, Payload: %s, TraceId: %s",
                    logSubject,
                    payload instanceof String ? payload : serializePayload(payload, log),
                    traceId));
        }
    }

    public void logApiResponsePayload(Logger log, String logSubject, String traceId, Object payload) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("LogSubject: %s, Payload: %s, TraceId: %s",
                    logSubject,
                    payload instanceof String ? payload : serializePayload(payload, log),
                    traceId));
        }
    }
}
