package io.hawt.example.spring.boot.camel.support;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@Builder
public class ApiRequestClientRequestContext {
    private String traceId;
    private String authToken;
}
