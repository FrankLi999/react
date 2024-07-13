package io.hawt.example.spring.boot.camel.dto;

import java.time.OffsetDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import org.springframework.http.HttpStatus;

@Data
@Accessors(chain = true)
@Builder
public class RequestContext {

	private Object apiRequest;

	private RequestHeader requestHeader;

	private String endpointId;

	private HttpStatus status;

	private OffsetDateTime requestTime;

	private String authToken;

}
