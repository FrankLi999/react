package mycamel.spring.boot.camel.dto;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

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
	private List<ResponseMessage> responseMessages = new ArrayList<>();
}
