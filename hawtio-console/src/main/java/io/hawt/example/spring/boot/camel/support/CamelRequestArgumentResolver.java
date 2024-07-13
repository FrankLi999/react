package io.hawt.example.spring.boot.camel.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class CamelRequestArgumentResolver implements HandlerMethodArgumentResolver {
    private final ObjectMapper objectMapper;
    private final CamelRequestArgumentResolverService camelRequestArgumentResolverService;
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterAnnotation(ValidCamelRequest.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String jsonPayload = objectMapper.writeValueAsString(objectMapper.readTree(getJsonPayload(nativeWebRequest)));
        ValidCamelRequest annotation = methodParameter.getParameterAnnotation(ValidCamelRequest.class);
        String endpointId = annotation.endpoint();
        String logSubject = annotation.logSubject();
        Class<?> type = annotation.type();
        String authToken = this.getHeader(nativeWebRequest, "MyCamel_AuthToken");

        return camelRequestArgumentResolverService.resolveArgument(jsonPayload, endpointId, logSubject, type, authToken);
    }

    private String getJsonPayload(NativeWebRequest nativeWebRequest) throws IOException {
        HttpServletRequest httpServletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        return StreamUtils.copyToString(httpServletRequest.getInputStream(), StandardCharsets.UTF_8);
    }
    private String getHeader(NativeWebRequest nativeWebRequest, String header) {
        return nativeWebRequest.getHeader(header);
    }
}
