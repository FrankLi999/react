package io.hawt.example.spring.boot.camel.support;

import org.slf4j.MDC;
import org.springframework.util.StringUtils;

public class LogHelper {
    public static void removeMdc() {
        MDC.remove(MyCamelConstants.MDC_MY_CAMEL_TRACE_ID);
        MDC.remove(MyCamelConstants.MDC_MY_CAMEL_ROUTE_ID);
    }

    public static void mdc(String traceId, String rouetId) {
        if (StringUtils.hasText(traceId)) {
            MDC.put(MyCamelConstants.MDC_MY_CAMEL_TRACE_ID, traceId);
        }
        if (StringUtils.hasText(rouetId)) {
            MDC.put(MyCamelConstants.MDC_MY_CAMEL_ROUTE_ID, rouetId);
        }
    }
}
