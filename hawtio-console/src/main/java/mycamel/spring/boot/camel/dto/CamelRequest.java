package mycamel.spring.boot.camel.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
public class CamelRequest<T> {

	RequestContext requestContext;

	private String httpMethod;

	private String httpUrl;

	private T requestBody;

	private String requestJson;

}
