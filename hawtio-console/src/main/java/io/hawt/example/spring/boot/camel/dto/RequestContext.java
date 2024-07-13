package io.hawt.example.spring.boot.camel.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.camel.resume.Offset;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Data
@Accessors(chain=true)
@Builder
public class RequestContext {
    private Object apiRequest;
    private RequestHeader requestHeader;
    private String endpointId;
    private HttpStatus status;
    private OffsetDateTime requestTime;
    private String authToken;
}
