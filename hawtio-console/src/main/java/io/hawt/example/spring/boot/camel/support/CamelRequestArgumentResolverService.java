package io.hawt.example.spring.boot.camel.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hawt.example.spring.boot.camel.dto.CamelRequest;
import io.hawt.example.spring.boot.camel.dto.RequestContext;
import io.hawt.example.spring.boot.camel.dto.RequestHeader;
import io.hawt.example.spring.boot.camel.exception.InvalidteRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.Validator;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ConstraintViolation;
import java.lang.reflect.Method;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class CamelRequestArgumentResolverService {
    @Value("${my-camel.validate.api.request}")
    private boolean validateApiRequest;
    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final Map<String, Method> methodCache = new ConcurrentHashMap<>();
    public Object resolveArgument(String jsonPayload, String endpointId, String logSubject, Class<?> type, String authToken) throws Exception {
        Object javaBody = null;
        try {
            javaBody = objectMapper.readValue(jsonPayload, type);
        } catch (JsonProcessingException e) {
            log.error("Error deserialize API request json.", e);
            throw new InvalidteRequestException(endpointId, e.getMessage(), e.getCause());
        }
        RequestHeader requestHeader = this.getRequestHeader(javaBody.getClass(), javaBody);
        RequestContext requestContext = RequestContext.builder()
                .apiRequest(javaBody)
                .requestHeader(requestHeader)
                .requestTime(OffsetDateTime.now())
                .endpointId(endpointId)
                .authToken(authToken)
                .build();
        setupRequestContext(requestHeader, authToken);
        // validate token

        if (validateApiRequest) {
            Set<ConstraintViolation<Object>> violations = validator.validate(javaBody);
            if (!violations.isEmpty()) {
                ConstraintViolationException ex = new ConstraintViolationException(violations);
                throws new InvalidteRequestException(endpointId, ex.getMessage(), ex.getCause());
            }
        }
        return CamelRequest.builder()
                .requestJson(jsonPayload)
                .requestContext(requestContext)
                .requestBody(javaBody)
                .build();
    }
    private RequestHeader getRequestHeader(Class<?> type, Object javaBody) {
        Method method = methodCache.computeIfAbsent(type.getName(), className -> ReflectionUtils.findMethod(type,"getRequestHeader"));
        return (RequestHeader) ReflectionUtils.invokeMethod(method, javaBody);
    }
    private void setupRequestContext(RequestHeader requestHeader, String authToken) {
        String traceId = requestHeader.getTraceId();
        ApiRequestClientRequestContext clientRequestContext = ApiRequestClientRequestContext.builder()
                .traceId(traceId)
                .authToken(authToken)
                .build();
        MDC.put("MY-CAMEL_TRACE_ID", traceId);
    }
}
